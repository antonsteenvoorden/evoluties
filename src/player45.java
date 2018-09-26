import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;


public class player45 implements ContestSubmission {
    Random rnd_;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;

    public player45() {
        rnd_ = new Random();
    }

    public void setSeed(long seed) {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation) {
        // Set evaluation problem used in the run
        evaluation_ = evaluation;

        // Get evaluation properties
        Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        // Property keys depend on specific evaluation
        // E.g. double param = double.parsedouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

        // Do sth with property values, e.g. specify relevant settings of your algorithm
        if (isMultimodal) {
            // Do sth
        } else {
            // Do sth else
        }
    }

    private  double getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        double randomValue = min + (max - min) * rnd_.nextDouble();
        return randomValue;
    }


    public void run() {
        // Run your algorithm here
        Maths matrixMath = new Maths();

        int evals = 0;

        //initialization
        int N = 10;
        double[][] _xmean = new double[N][1];
        for(int i=0; i<N; i++){
          _xmean[i][0] = getRandomNumberInRange(-5, 5);
        }
        Matrix xmean = new Matrix(_xmean);
        double sigma = 0.3;
        int lambda = 4+(int)Math.floor(3*Math.log(N));
        lambda = 100;
        int mu = (int)Math.floor(lambda/2);

        double[][] _weigths = new double[mu][1];
        for(int i=0; i<mu; i++){
          _weigths[i][0] = Math.log(mu+1/2) - Math.log(i+1);
          // _weigths[i][0] = mu + 1 - i;
        }
        Matrix weigths = new Matrix(_weigths);
        weigths = matrixMath.normalize(weigths);
        Matrix weigths_quadratic = weigths.power2();
        double mu_eff  = matrixMath.sum(weigths)/matrixMath.sum(weigths_quadratic);

        //adaption
        double cc = (4+mu_eff/N) / (N+4+2*mu_eff/N);
        double cs = (mu_eff+2) / (N+mu_eff+5);
        double c1 = 2 / (Math.pow((N+1.3),2) + mu_eff);
        double cmu = Math.min(1-c1, 2* (mu_eff-2+1/mu_eff) / (Math.pow((N+2),2)+mu_eff));
        double damps = 1 + 2 * Math.max(0, Math.sqrt((mu_eff-1) / (N+1))-1) + cs;
        boolean hsig;

        Matrix pc = new Matrix(N, 1, 0);
        Matrix ps = new Matrix(N, 1, 0);
        Matrix B = matrixMath.identity(N);
        Matrix D = new Matrix(N, 1, 1);
        Matrix C = B.times(D.power2().diagonal(false)).times(B.transpose());
        Matrix C_invsqrt = B.times(D.powerinverse().diagonal(false)).times(B.transpose());

        int eigeneval = 0;
        double chiN = Math.pow(N, 0.5)*(1-1/(4*N) + 1/(21*Math.pow(N,2)));

        int generations = 0;
        while (evals < evaluations_limit_/*generations <1*/) {
          generations += 1;
          //here we actually start..
          ArrayList<CandidateSolution> population = new ArrayList<CandidateSolution>();
          for(int i=0; i<lambda; i++){
            CandidateSolution child = new CandidateSolution(rnd_);
            double[][] _tmp = new double[N][1];
            for(int j=0; j<N; j++){
              _tmp[j][0] = rnd_.nextGaussian() * D.get(j, 0);
            }
            Matrix tmp = new Matrix(_tmp);

            Matrix new_x = xmean.plus(B.times(sigma).times(tmp));
            double[] new_genome = new double[N];
            for(int j=0; j<N; j++){
              new_genome[j] = new_x.get(j, 0);
            }
            child.genotype = new_genome;
            child.fitness = (double)evaluation_.evaluate(child.genotype);
            evals += 1;
            population.add(child);
          }
          Collections.sort(population);

          Matrix xold = xmean.copy();
          for(int i=0; i<N; i++){
            double result = 0;
            for(int j=0 ;j<mu; j++){
              result += population.get(j).genotype[i] * weigths.get(j,0);
            }
            xmean.set(i, 0,result);
          }

          ps = ps.times((1-cs)).plus(C_invsqrt.times(xmean.minus(xold)).times(Math.sqrt(cs*(2-cs)*mu_eff)).times(1/sigma));
          hsig = ps.norm()/Math.sqrt(1-Math.pow((1-cs),(2*evals/lambda)))/chiN < 1.4 + 2/(N+1);

          pc = pc.times((1-cc));
          if(hsig) pc = xmean.minus(xold).times(Math.sqrt(cc*(2-cc)*mu_eff)).times(1/sigma);

          double[][] tmp = new double[N][mu];
          for(int i=0; i<N; i++){
            for(int j=0; j<mu; j++){
              tmp[i][j] = population.get(j).genotype[i] - xold.get(i,0);
            }
          }
          Matrix artmp = new Matrix(tmp);
          artmp = artmp.times(1/sigma);

          Matrix oldC = C;
          C = oldC.times((1-c1-cmu)).plus(pc.times(pc.transpose()).times(c1)).plus(artmp.times(weigths.diagonal(false)).times(artmp.transpose()).times(cmu));
          if(!hsig) C = C.plus(oldC.times(c1*(cc*(2-cc))));

          sigma = sigma * Math.exp((cs/damps)*(ps.norm()/chiN -1));
          if(evals - eigeneval > lambda/(c1+cmu)/N/10){
            // eigeneval = evals;
            // C = C.triu(0).plus(C.triu(1).transpose());
            // EigenvalueDecomposition ED = C.eig();
            // B = ED.getV();
            // D = ED.getD().diagonal(true).sqrt();
            // C_invsqrt = B.times(D.powerinverse().diagonal(false)).times(B.transpose());
          }
        }
    }
}

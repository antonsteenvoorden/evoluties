import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.Arrays;

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
        // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
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

        double randomValue = min + (max - min) * this.random.nextDouble();
        return randomValue;
    }

    public void run() {
        // Run your algorithm here
        int evals = 0;
        int generations = 0;

        //initialization
        int N = 10;
        double[] xmean = new double[N];
        for(int i=0; i<N; i++){
          xmean[i] = getRandomNumberInRange(-5, 5);
        }

        double sigma = 0.3;
        int lambda = 4+(int)Math.floor(3*Math.log(N));
        int mu = (int)Math.floor(lambda/2);
        double[] weigths = new double[mu];
        double weights_total = 0;
        double weights_2_total = 0;
        for(int i=0; i<mu; i++){
          double _w =  Math.log(mu+1/2) - Math.log(i+1);
          // double _w = mu - i + 1;
          weights_total += _w;
          weights_2_total += (_w * _w);
          weigths[i] = _w;
        }
        for(int i=0; i<mu; i++){
          weigths[i] = weigths[i] /weights_total;
        }
        double mu_eff  = (weights_total*weights_total)/weights_2_total;

        //adaption
        double cc = (4+mu_eff/N) / (N+4+2*mu_eff/N);
        double cs = (mu_eff+2) / (N+mu_eff+5);
        double c1 = 2 / (Math.pow((N+1.3),2) + mu_eff);
        double cmu = Math.min(1-c1, 2* (mu_eff-2+1/mu_eff) / (Math.pow((N+2),2)+mu_eff));
        double damps = 1 + 2 * Math.max(0, Math.sqrt((mu_eff-1) / (N+1))-1) + cs;

        double[] pc = new double[N];
        double[] ps = new double[N];
        double[][] B = new double[N][N];
        double[] D = new double[N];
        double[][] C = new double[N][N];
        double[][] C_invsqrt = new double[N][N];
        for(int i=0; i<N; i++){
          pc[i] = 0;
          ps[i] = 0;
          B[i][i] = 1;
          D[i] = 1;
          C[i][i] = 1;
          C_invsqrt[i][i] = 1;
        }
        int eigeneval = 0;
        double chiN = Math.pow(N, 0.5)*(1-1/(4*N) + 1/(21*Math.pow(N,2)));

        while (evals < evaluations_limit_) {
          //here we actually start..
          Arraylist<Double[]> children = new ArrayList<Double[]>();
          for(int i=0; i<lambda; i++){
            children.add()
          }

        }
    }
}

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

    private double[] vector_times_vector(double[] v1, double[] v2){
      double[] result = new double[v1.length];
      for(int i=0; i<v1.length; i++){
        result[i] = v1[i]*v2[i];
      }
      return result;
    }


    private double[] matrix_times_vector(double[][] m, double[] v){
      double[] result = new double[m.length];
      for(int i=0; i<m.length; i++){
        double _result = 0;
        for(int j=0; j<m[0].length; j++){
          _result += m[i][j] * v[j];
        }
        result[i] = _result;
      }
      return result;
    }



    private double[][] x_times_matrix(double x, double[][] m){
      for(int i=0; i<m.length; i++){
        for(int j=0; j<m[0].length; j++){
          m[i][j] = m[i][j] * x;
        }
      }
      return m;
    }

    private double[] x_times_vector(double x, double[] v){
      for(int i=0; i<v.length; i++){
        v[i] = v[i] * x;
      }
      return v;
    }

    private double[] vector_plus_vector(double[] v1, double[] v2){
      double[] result = new double[v1.length];
      for(int i=0; i<v1.length; i++){
        result[i] = v1[i] + v2[i];
      }
      return result;
    }

    private double[] vector_minus_vector(double[] v1, double[] v2){
      double[] result = new double[v1.length];
      for(int i=0; i<v1.length; i++){
        result[i] = v1[i] - v2[i];
      }
      return result;
    }

    private double[][] matrix_plus_matrix(double[][] m1, double[][] m2){
      double[][] result = new double[m1.length][m1[0].length];
      for(int i=0; i<m1.length; i++){
        for(int j=0; j<m2[0].length; j++){
          result[i][j] = m1[i][j] + m2[i][j];
        }
      }
      return result;
    }


    public static double norm(double[] array) {

    if (array != null) {
        int n = array.length;
        double sum = 0.0;

        for (int i = 0; i < n; i++) {
            sum += array[i] * array[i];
        }
        return Math.pow(sum, 0.5);
    } else
        return 0.0;
      }

      public double[][] diagonal(double[] v1){
        double[][] result = new double[v1.length][v1.length];
        for(int i=0; i<v1.length; i++){
          for(int j=0; j<v1.length; j++){
            if(i == j){
              result[i][j] = v1[i];
            }else{
              result[i][j] = 0;
            }
          }
        }
        return result;
      }

      public static double[][] matrix_times_matrix(double[][] m1, double[][] m2) {
    int m1ColLength = m1[0].length; // m1 columns length
    int m2RowLength = m2.length;    // m2 rows length
    if(m1ColLength != m2RowLength) return null; // matrix multiplication is not possible
    int mRRowLength = m1.length;    // m result rows length
    int mRColLength = m2[0].length; // m result columns length
    double[][] mResult = new double[mRRowLength][mRColLength];
    for(int i = 0; i < mRRowLength; i++) {         // rows from m1
        for(int j = 0; j < mRColLength; j++) {     // columns from m2
            for(int k = 0; k < m1ColLength; k++) { // columns from m1
                mResult[i][j] += m1[i][k] * m2[k][j];
            }
        }
    }
    return mResult;
}



    public static double[][] transpose(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
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
        boolean hsig;

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
          ArrayList<CandidateSolution> population = new ArrayList<CandidateSolution>();
          for(int i=0; i<lambda; i++){
            CandidateSolution child = new CandidateSolution(rnd_);
            double[] randn = new double[N];
            for(int j=0; j<N; j++){
              randn[j] = rnd_.nextGaussian();
            }
            double[] new_genome = vector_plus_vector(xmean, x_times_vector(sigma, matrix_times_vector(B, vector_times_vector(D, randn))));
            child.genotype = new_genome;
            child.fitness = (double)evaluation_.evaluate(child.genotype);
            evals += 1;
            population.add(child);
          }
          Collections.sort(population);
          System.out.println(population.get(0).fitness);

          double[] xold = xmean;

          for(int i=0; i<xmean.length; i++){
            xmean[i] = 0;
          }

          for(int i=0; i<mu; i++){
            for(int j=0; j<N; j++){
              xmean[j] = xmean[j] + weigths[i] * population.get(i).genotype[j];
            }
          }

          ps = vector_plus_vector(x_times_vector((1-cs), ps), x_times_vector(Math.sqrt(cs*(2-cs)*mu_eff)/sigma, matrix_times_vector(C_invsqrt, vector_minus_vector(xmean, xold))));
          hsig = norm(ps)/Math.sqrt(1-Math.pow((1-cs),(2*evals/lambda)))/chiN < (1.4 + 2/(N+1));
          pc = x_times_vector((1-cc), pc);
          if(hsig){
            pc = vector_plus_vector(pc, x_times_vector(Math.sqrt(cc*(2-cc)*mu_eff)/sigma, vector_minus_vector(xmean, xold)));
          }

          double[][] tmp = new double[N][mu];
          for(int i=0; i<N; i++){
            for(int j=0; j<mu; j++){
              tmp[i][j] = population.get(j).genotype[i] - xold[i];
            }
          }
          tmp = x_times_matrix((1/sigma), tmp);

          double[][] term1 =  x_times_matrix((1-c1-cmu), C);
          double[][] pc_matrix = new double[N][N];
          for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
              pc_matrix[i][j] = pc[i]*pc[j];
            }
          }
          if(!hsig){
            pc_matrix = matrix_plus_matrix(pc_matrix, x_times_matrix((cc*(2-cc)), C));
          }
          double[][] term2 = x_times_matrix(c1, pc_matrix);
          double[][] term3 = x_times_matrix(cmu, matrix_times_matrix(matrix_times_matrix(tmp, diagonal(weigths)), transpose(tmp)));
          C = matrix_plus_matrix(matrix_plus_matrix(term1, term2), term3);

          sigma = sigma * Math.exp((cs/damps)*(norm(ps)/chiN -1));

          if(evals - eigeneval > lambda/(c1+cmu)/N/10){
            eigeneval = evals;
            double[][] C_trans = transpose(C);
            for(int i=0; i<C.length; i++){
              for(int j=0; j<C[0].length; j++){
                if(j < i){
                  C[i][j] = C_trans[i][j];
                }
              }
            }

          }

        }
    }
}

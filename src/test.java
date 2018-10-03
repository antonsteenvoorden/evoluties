import java.lang.Runtime;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.Collections;
import java.util.ArrayList;

import java.util.Random;

class test   {


    public static void main(String[] args){

      Random rnd_ = new Random();
      rnd_.setSeed(1);

      try{
        Process proc1 = Runtime.getRuntime().exec("javac -cp contest.jar testplayer.java CandidateSolution.java testCandidateSolution.java Matrix.java CholeskyDecomposition.java EigenvalueDecomposition.java LUDecomposition.java Maths.java QRDecomposition.java SingularValueDecomposition.java");
        Process proc2 = Runtime.getRuntime().exec("jar cmf testMainClass.txt submission.jar testplayer.class CandidateSolution.class testCandidateSolution.class Matrix.class CholeskyDecomposition.class EigenvalueDecomposition.class LUDecomposition.class Maths.class QRDecomposition.class SingularValueDecomposition.class");
      } catch (IOException e){

      }

      // Run your algorithm here
      Maths matrixMath = new Maths();

      int evals = 0;
      int evaluations_limit_ = 300;

      //initialization
      int N = 3;
      double[][] _xmean = new double[N][1];
      _xmean[0][0] = 100.0;
      _xmean[1][0] = 50.0;
      _xmean[2][0] = 4.0;
      Matrix xmean = new Matrix(_xmean);
      double sigma = 4;
      int lambda = 4+(int)Math.floor(3*Math.log(N));
      // lambda = 100;
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
        ArrayList<testCandidateSolution> population = new ArrayList<testCandidateSolution>();
        for(int i=0; i<lambda; i++){
          testCandidateSolution child = new testCandidateSolution();
          double[][] _tmp = new double[N][1];
          for(int j=0; j<N; j++){
            _tmp[j][0] = rnd_.nextGaussian() * D.get(j, 0);
          }
          Matrix tmp = new Matrix(_tmp);

          Matrix new_x = xmean.plus(B.times(sigma).times(tmp));
          child.lambda = new_x.get(0,0);
          child.mu = new_x.get(1,0);
          child.sigma = new_x.get(2,0);
          child.fitness = evaluate(child);
          evals += 1;
          population.add(child);
        }
        Collections.sort(population);

        Matrix xold = xmean.copy();

        double result = 0;
        for(int j=0 ;j<mu; j++){
          result += population.get(j).lambda * weigths.get(j,0);
        }
        xmean.set(0, 0,result);
        result = 0;
        for(int j=0 ;j<mu; j++){
          result += population.get(j).mu * weigths.get(j,0);
        }
        xmean.set(1, 0,result);
        result = 0;
        for(int j=0 ;j<mu; j++){
          result += population.get(j).sigma * weigths.get(j,0);
        }
        xmean.set(2, 0,result);


        ps = ps.times((1-cs)).plus(C_invsqrt.times(xmean.minus(xold)).times(Math.sqrt(cs*(2-cs)*mu_eff)).times(1/sigma));
        hsig = ps.norm()/Math.sqrt(1-Math.pow((1-cs),(2*evals/lambda)))/chiN < 1.4 + 2/(N+1);

        pc = pc.times((1-cc));
        if(hsig) pc = xmean.minus(xold).times(Math.sqrt(cc*(2-cc)*mu_eff)).times(1/sigma);

        double[][] tmp = new double[N][mu];

        for(int j=0; j<mu; j++){
          tmp[0][j] = population.get(j).lambda - xold.get(0,0);
        }
        for(int j=0; j<mu; j++){
          tmp[1][j] = population.get(j).mu - xold.get(1,0);
        }
        for(int j=0; j<mu; j++){
          tmp[2][j] = population.get(j).sigma - xold.get(2,0);
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

    public static double evaluate(testCandidateSolution sol){
      int lambda = (int)sol.lambda;
      int mu = (int)sol.mu;
      if(mu > lambda/2 || mu < 0) mu = (int)Math.floor(lambda/2);
      double sigma = sol.sigma;
      if(sigma < 0) sigma = Math.abs(sigma);

      String command = String.format("java -Dlambda=%d -Dmu=%d -Dsigma=%.2f -jar testrun.jar -submission=testplayer -evaluation=BentCigarFunction -seed=1", lambda, mu, sigma);
      System.out.println(command);
      try{
        Process proc = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        // String s = null;
        // while((s = stdInput.readLine()) != null){
        //   System.out.println(s);
        // }
        //
        // s = null;
        // while((s = stdError.readLine()) != null){
        //   System.out.println(s);
        // }
        // return 0.0;

        String s = stdInput.readLine();
        System.out.println(s);
        System.out.println(s.split(" ")[1]);
        return  Double.parseDouble(s.split(" ")[1]);
      }catch(IOException e){
        System.out.println("Da ging nie goe");
      }
      return 0.0;
    }
}

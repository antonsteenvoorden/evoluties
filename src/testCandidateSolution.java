import java.util.Random;
import java.util.Arrays;

public class testCandidateSolution implements Comparable<testCandidateSolution>{
    double lambda;
    double mu;
    double sigma;
    double fitness;


    public testCandidateSolution() {
        this.lambda = 0.0;
        this.mu = 0.0;
        this.sigma = 0.0;
        this.fitness = Double.MIN_VALUE;
    }

    public int compareTo(testCandidateSolution sol){
      if(this.fitness > sol.fitness){
        return -1;
      }
      if(this.fitness < sol.fitness){
        return 1;
      }
      return 0;
    }

}

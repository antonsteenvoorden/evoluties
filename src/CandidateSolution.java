import java.util.Random;
import java.util.Arrays;

public class CandidateSolution implements Comparable<CandidateSolution>{
    double[] genotype;
    double fitness;


    public CandidateSolution(Random random) {
        this.genotype = new double[10];
        this.fitness = Double.MIN_VALUE;
    }

    public int compareTo(CandidateSolution sol){
      if(this.fitness > sol.fitness){
        return -1;
      }
      if(this.fitness < sol.fitness){
        return 1;
      }
      return 0;
    }

}

import java.util.Random;
import java.util.Arrays;

public class CandidateSolution implements Comparable<CandidateSolution>{
    double[] genotype;
    double[] phenotype;
    double fitness;
    double mutationChance;
    double gaussianStandardDeviation;
    Random random;
    private static int minAllele = -5;
    private static int maxAllele = 5;


    public CandidateSolution(Random random, double mutationChance, double gaussianStandardDeviation) {
        this.genotype = new double[10];
        this.fitness = Double.MIN_VALUE;
        this.random = random;
        this.mutationChance = mutationChance;
        if(this.mutationChance == -1 ) {
            this.mutationChance = 0.2; //default
        }
        // doe random shit met range
        this.gaussianStandardDeviation = gaussianStandardDeviation;
        if(this.gaussianStandardDeviation == -1 ) {
          this.gaussianStandardDeviation = 0.1; //default
        }
        init();
    }

    public void init() {
        for (int i = 0; i < 10; i++) {
            this.genotype[i] = getRandomNumberInRange(minAllele, maxAllele);
        }
    }

    public String printSolution(){
        String output_string = "";
        for(double allele: this.genotype){
          output_string = output_string + Double.toString(allele) + ",";
        }
        return output_string + this.fitness;
    }

    public double[] getGenotype(){
      return this.genotype;
    }
    public double getGenomeAtIndex(int index) {
        if(index < this.genotype.length -1) {
            return this.genotype[index];
        } else {
            //ERROR
        }
        return 0;
    }

    public double[] getHead(int cutOff){
      return Arrays.copyOfRange(this.genotype, 0, cutOff);
    }

    public double[] getTail(int cutOff){
      return Arrays.copyOfRange(this.genotype, cutOff, genotype.length);
    }

    public void setGenotype(double[] genotype){
      this.genotype = genotype;
    }

    public void setFitness(double fit){
      this.fitness = fit;
    }

    public double getFitness(){
      return this.fitness;
    }



    private  double getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        double randomValue = min + (max - min) * this.random.nextDouble();
        return randomValue;
    }



    public void mutate() {
      for(int i=0; i<genotype.length; i++){
        if(this.random.nextDouble() < this.mutationChance){
          genotype[i] += this.random.nextGaussian() * this.gaussianStandardDeviation;
        }
      }
    }

    public String toString(){
      String result = "{ ";
      for(double allele : this.genotype){
        result += allele;
        result += " ";
      }
      result += "}";
      return result;
    }

    // gets called by Collection sort
    public int compareTo(CandidateSolution sol1) {
      if(this.getFitness() > sol1.getFitness()){
        return -1;
      }else if(this.getFitness() < sol1.getFitness()){
        return 1;
      }
      return 0;
    }


    public boolean equals(CandidateSolution other){
      if (other == this) return true;
      return false;
    }
}

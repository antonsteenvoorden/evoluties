import java.util.Random;

public class CandidateSolution {
    double[] genotype;
    double[] phenotype;
    private static int minAllele = -5;
    private static int maxAllele = 5;


    public CandidateSolution() {
        this.genotype = new double[10];
        this.phenotype = genotype; // zoiets
        // doe randmo shit met range
        init();
    }
    private static double getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }
    public void init() {
        for (int i = 0; i < 10; i++) {
            this.genotype[i] = getRandomNumberInRange(minAllele, maxAllele);
        }
    }


    public void mutate() {

    }

}

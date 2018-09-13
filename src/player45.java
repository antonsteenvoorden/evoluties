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

    private static double getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }

//    public class CandidateSolution {
//        double[] genotype;
//        double[] phenotype;
//
//		public CandidateSolution() {
//            this.genotype = new double[10];
//            this.phenotype = genotype; // zoiets
//            // doe randmo shit met range
//            init();
//        }
//		public void init() {
//            for (int i = 0; i < 10; i++) {
//                this.genotype[i] = getRandomNumberInRange(minAllele, maxAllele);
//            }
//        }
//
//
//		public void mutate() {
//
//        }
//
//    }

//    public class Population {
//        CandidateSolution[] population;
//        int populationSize;
//        int offSpringCount;
//
//        Population(int populationSize, int offSpringCount){
//            this.population = new CandidateSolution[populationSize];
//            this.populationSize = populationSize;
//            this.offSpringCount = offSpringCount;
//        }
//
//        // determine parents based on probability/fitness
//        public CandidateSolution[] parentSelection () {
//            return null;
//        }
//
//        // after sex determine which candidates survive to next gen
//        public CandidateSolution[] survivorSelection () {
//            return null;
//        }
//
//        //crossover -> mutate new clones
//        public CandidateSolution[] sex () {
//            return null;
//        }
//
//        public CandidateSolution[] CrossOver (CandidateSolution c1, CandidateSolution c2){
//            return null;
//        }
//
//        public void init (){
//            for (int p = 0; p < this.populationSize; p++) {
//                CandidateSolution tmpSolution = new CandidateSolution();
//                this.population[p] = tmpSolution;
//            }
//        }
//    }

    public void run() {
        // Run your algorithm here
        final int min = -5;
        final int max = 5;
        final int populationSize = 100;
        final int offSpringCount = 2;

        int evals = 0;

        // init population
        Population population = new Population(populationSize, offSpringCount);
        System.out.println(population.population.length);
        System.out.println(Arrays.toString(population.population[0].genotype));
        System.out.println(Arrays.toString(population.population[1].genotype));

        // calculate fitness
        while (evals < evaluations_limit_) {
            // Select parents
            // Apply crossover / mutation operators
            double child[] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child);
            System.out.println("fitness is");
            System.out.println(fitness);
            evals += evaluations_limit_;
            evals++;
            // Select survivors
        }
    }
}

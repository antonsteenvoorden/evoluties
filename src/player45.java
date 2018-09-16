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

    public void run() {
        // Run your algorithm here
        final int min = -5;
        final int max = 5;
        final int populationSize = 100;
        final int offSpringCount = 2;

        int evals = 0;
        int generation = 0;

        // init population
        Population population = new Population(populationSize, offSpringCount, evaluation_, rnd_);
        evals += populationSize;
        // calculate fitness
        while (evals < evaluations_limit_) {
            System.out.println(generation);
            population.generation();
            generation++;
            evals += offSpringCount;
            // Double fitness = (double) evaluation_.evaluate(child);
            // System.out.println("fitness is");
            // System.out.println(fitness);
            // evals += evaluations_limit_;
            // Select survivors
        }
    }
}

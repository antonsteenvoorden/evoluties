import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;

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

        double bash_input[] = new double[4];
        try { bash_input[0] = Double.parseDouble(System.getProperty("pop_size")); } catch (NullPointerException e){ bash_input[0] = 100; }
        try { bash_input[1] = Double.parseDouble(System.getProperty("n_par")); } catch (NullPointerException e) { bash_input[1] = 5; }
        try { bash_input[2] = Double.parseDouble(System.getProperty("m_chance")); } catch (NullPointerException e) { bash_input[2] = 0.1; }
        try { bash_input[3] = Double.parseDouble(System.getProperty("gs_dev")); } catch (NullPointerException e) { bash_input[3] = 1; }

        System.out.println("Input parameters:");
        int i;
        for (i=0; i < bash_input.length; i++) {
            System.out.println(bash_input[i]);
        }
        System.out.println();

        final int populationSize = (int)bash_input[0];
        final int numberOfParentsSelections = (int)bash_input[1];

        final double mutationChance = bash_input[2];
        final double gaussianStandardDeviation = bash_input[3];
        final int min = -5;
        final int max = 5;

        int evals = 0;
        int generation = 0;

        // init population
        Population population = new Population(populationSize, numberOfParentsSelections, mutationChance, gaussianStandardDeviation, evaluation_, rnd_);
        evals += populationSize;
        // calculate fitness
        while (evals < evaluations_limit_) {
            population.createNewGeneration();
            generation++;
            evals += numberOfParentsSelections*2;
        }
    }
}
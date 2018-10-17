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

        double bash_input[] = new double[7];
        try { bash_input[0] = Double.parseDouble(System.getProperty("pop_size")); } catch (Exception e){ bash_input[0] = 100; }
        try { bash_input[1] = Double.parseDouble(System.getProperty("n_par")); } catch (Exception e) { bash_input[1] = 2; }
        try { bash_input[2] = Double.parseDouble(System.getProperty("m_chance")); } catch (Exception e) { bash_input[2] = 0.1; }
        try { bash_input[3] = Double.parseDouble(System.getProperty("gs_dev")); } catch (Exception e) { bash_input[3] = 0.1; }
        try { bash_input[4] = Double.parseDouble(System.getProperty("par_sel")); } catch (Exception e) { bash_input[4] = 2; }
        try { bash_input[5] = Double.parseDouble(System.getProperty("sur_sel")); } catch (Exception e) { bash_input[5] = 0; }
        try { bash_input[6] = Double.parseDouble(System.getProperty("rec_ope")); } catch (Exception e) { bash_input[6] = 0; }

        final int populationSize = (int)bash_input[0];
        final int numberOfParents = (int)bash_input[1];

        final int numberOfParentSelections = (int)Math.ceil(bash_input[0] / bash_input[1]);
        //final int numberOfParentSelections = Math.ceil((double)(populationSize / numberOfParents));

        final double mutationChance = bash_input[2];
        final double gaussianStandardDeviation = bash_input[3];
        final int min = -5;
        final int max = 5;

        int evals = 0;
        int generation = 0;

        int numberOfPrints = 100;
        int currentEvals = 0;
        int printSplit = evaluations_limit_ / numberOfPrints;

        int parentSelection = (int)bash_input[4];
        int survivorSelection = (int)bash_input[5];
        int recombinationOperator = (int)bash_input[6];

        // init population
        Population population = new Population(populationSize, numberOfParents, numberOfParentSelections, mutationChance, gaussianStandardDeviation, evaluation_, rnd_, parentSelection, survivorSelection, recombinationOperator);
        evals += populationSize;
        // calculate fitness
        population.printPopulation();
        while (evals < evaluations_limit_) {
            population.createNewGeneration();
            generation++;
            evals += populationSize; //Check if this is still right with number of children.
            currentEvals += populationSize;
            if(currentEvals >= printSplit){
              currentEvals = 0;
              population.printPopulation();
            }
        }
    }
}

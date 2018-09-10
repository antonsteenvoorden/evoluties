import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.Arrays;

public class player45 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
	public player45()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
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
        if(isMultimodal){
            // Do sth
        }else{
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

    public double[][] init(int min, int max, int populationSize) {
		double[][] parents = new double[populationSize][10];
		for(int p = 0; p < populationSize; p++) {
			double[] tmpParent = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			for (int i = 0; i < 10; i++) {
				tmpParent[i] = getRandomNumberInRange(min, max);
			}
			parents[p] = tmpParent;
		}
		return parents;
	}

	public void run()
	{
		// Run your algorithm here
        final int min = -5;
		final int max = 5;
		final int populationSize = 100;

        int evals = 0;

        // init population
		double[][] population = init(min, max, 2);
		System.out.println(population.length);
		System.out.println(Arrays.toString(population[0]));
		System.out.println(Arrays.toString(population[1]));

        // calculate fitness
        while(evals<evaluations_limit_){
            // Select parents
            // Apply crossover / mutation operators
            double child[] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child);
			System.out.println("fitness is");
			System.out.println(fitness);
			evals+=evaluations_limit_;
            evals++;
            // Select survivors
        }
	}
}

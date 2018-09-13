import java.util.Random;
import java.util.Properties;
import java.util.Arrays;

public class Population {
    CandidateSolution[] population;
    int populationSize;
    int offSpringCount;

    Population(int populationSize, int offSpringCount){
        this.population = new CandidateSolution[populationSize];
        this.populationSize = populationSize;
        this.offSpringCount = offSpringCount;
        init();
    }

    // determine parents based on probability/fitness
    public CandidateSolution[] parentSelection () {
        return null;
    }

    // after sex determine which candidates survive to next gen
    public CandidateSolution[] survivorSelection () {
        return null;
    }

    //crossover -> mutate new clones
    public CandidateSolution[] sex () {
        return null;
    }

    public CandidateSolution[] CrossOver (CandidateSolution c1, CandidateSolution c2){
        return null;
    }

    public void init (){
        for (int p = 0; p < this.populationSize; p++) {
            CandidateSolution tmpSolution = new CandidateSolution();
            this.population[p] = tmpSolution;
        }
    }
}

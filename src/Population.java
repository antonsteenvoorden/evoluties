import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

import org.vu.contest.ContestEvaluation;

public class Population {
    ArrayList<CandidateSolution> population;
    ContestEvaluation evaluation;
    int populationSize;
    int numberOfParentsSelections;
    double mutationChance;
    double gaussianStandardDeviation;
    double bestFitness;
    CandidateSolution bestCandidate;
    Random random;

    // enums
    ParentSelection parentSelectionMethod;
    SurvivorSelection survivorSelectionMethod;

    Population(int populationSize, int numberOfParentsSelections, double mutationChance, double gaussianStandardDeviation, ContestEvaluation evaluation, Random random){
        this.population = new ArrayList<CandidateSolution>();
        this.evaluation = evaluation;
        this.populationSize = populationSize;
        this.numberOfParentsSelections = numberOfParentsSelections;
        this.mutationChance = mutationChance;
        this.gaussianStandardDeviation = gaussianStandardDeviation;
        this.bestCandidate = null;
        this.bestFitness = -9000;
        this.random = random;
        this.parentSelectionMethod = ParentSelection.RANDOM;
        this.survivorSelectionMethod = SurvivorSelection.REMOVE_WORST;

        init();
    }

    public void init (){
        for (int p = 0; p < this.populationSize; p++) {
            CandidateSolution tmpSolution = new CandidateSolution(this.random, this.mutationChance, this.gaussianStandardDeviation);
            this.population.add(tmpSolution);
        }
        evaluateChildren(this.population);
        Collections.sort(this.population);
    }

    // evaluates all our candidate solutions
    // assigns a fitness to each
    // keeps track of our best performing solution
    public void evaluateChildren(ArrayList<CandidateSolution> children){
      for(CandidateSolution sol : children){
        double fitness = (double)this.evaluation.evaluate(sol.getGenotype());
        if(fitness > this.bestFitness){
          this.bestFitness = fitness;
          this.bestCandidate = sol;
        }
        sol.setFitness(fitness);
      }
    }

    // select parents
    // make children
    // select best 100 as survivors
    public void createNewGeneration(){
        ArrayList<CandidateSolution> newChildren = new ArrayList<>();

        for(int i=0; i< this.numberOfParentsSelections; i++){
          CandidateSolution[] parents = parentSelection(this.parentSelectionMethod);
          CandidateSolution[] children = generateChildren(parents);
          newChildren.add(children[0]);
          newChildren.add(children[1]);
      }
      evaluateChildren(newChildren);

      if(this.survivorSelectionMethod != SurvivorSelection.REMOVE_WORST) {
        ArrayList<CandidateSolution> tmpSolutions = new ArrayList<>(this.population);
        tmpSolutions.addAll(newChildren);
        newChildren = tmpSolutions;
      }
      survivorSelection(newChildren);
    }

    public CandidateSolution[] parentSelection(ParentSelection parentSelection) {
        if(parentSelection == ParentSelection.RANDOM) {
            return randomParentSelection();
        }
        return new CandidateSolution[]{};
    }
    //best 2 out for random 5
    public CandidateSolution[] randomParentSelection() {
      CandidateSolution[] randomFive = new CandidateSolution[5];
      for(int i=0; i<5; i++){
        randomFive[i] = this.population.get(this.random.nextInt(this.population.size()));
      }

      CandidateSolution best = randomFive[0];
      CandidateSolution secondBest = randomFive[0];
      for(CandidateSolution sol : randomFive){
        if(sol.getFitness() > best.getFitness()){
          secondBest = best;
          best = sol;
        }else if(sol.getFitness() > secondBest.getFitness()){
          secondBest = sol;
        }
      }

      CandidateSolution[] parents = new CandidateSolution[]{best, secondBest};
      return parents;
    }

    //crossover
    public CandidateSolution[] generateChildren (CandidateSolution[] parents) {
        CandidateSolution firstChild = crossOver(parents[0], parents[1]);
        CandidateSolution secondChild = crossOver(parents[1], parents[0]);
        firstChild.mutate();
        secondChild.mutate();
        CandidateSolution[] children = new CandidateSolution[]{firstChild, secondChild};
        return children;
    }

    public CandidateSolution crossOver (CandidateSolution c1, CandidateSolution c2){
      CandidateSolution child = new CandidateSolution(this.random, this.mutationChance, this.gaussianStandardDeviation);
      int cutOff = randInt(0, 10);
      child.setGenotype(concatenateArrays(c1.getHead(cutOff), c2.getTail(cutOff)));
      return child;
    }

    // remove the worst!
    public void survivorSelection (ArrayList<CandidateSolution> solutions) {
        // ff geen switch
        if(this.survivorSelectionMethod == SurvivorSelection.REMOVE_WORST){
            this.population.subList(this.population.size() - this.numberOfParentsSelections*2, this.population.size()).clear();
            for(CandidateSolution sol: solutions){
                this.population.add(sol);
            }
        } else if(this.survivorSelectionMethod == SurvivorSelection.TOURNAMENT){
            // pick 2 at random, add the one with the highest fitness to the population.
        }

      Collections.sort(this.population);
    }

    public double[] concatenateArrays(double[]... arrays){
        int length = 0;
        for (double[] array : arrays) {
            length += array.length;
        }
        double[] result = new double[length];

        int offset = 0;
        for (double[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public int randInt(int min, int max) {
      return this.random.nextInt((max - min) + 1) + min;
    }


}

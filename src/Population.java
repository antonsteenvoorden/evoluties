import java.lang.reflect.Array;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

import org.vu.contest.ContestEvaluation;

public class Population {
    ArrayList<CandidateSolution> population;
    ContestEvaluation evaluation;
    int populationSize;
    int numberOfParents;
    int tournamentSize;
    int numberOfParentSelections;
    double mutationChance;
    double gaussianStandardDeviation;
    double bestFitness;
    CandidateSolution bestCandidate;
    Random random;

    // enums
    ParentSelection parentSelectionMethod;
    SurvivorSelection survivorSelectionMethod;
    RecombinationOperator recombinationOperator;

    Population(int populationSize, int numberOfParents, int numberOfParentSelections, double mutationChance, double gaussianStandardDeviation, ContestEvaluation evaluation, Random random){
        this.population = new ArrayList<CandidateSolution>();
        this.evaluation = evaluation;
        this.populationSize = populationSize;
        this.numberOfParents = numberOfParents;
        this.numberOfParentSelections = numberOfParentSelections;
        this.mutationChance = mutationChance;
        this.gaussianStandardDeviation = gaussianStandardDeviation;
        this.bestCandidate = null;
        this.bestFitness = -9000;
        this.random = random;

        this.tournamentSize = 4;

        // TODO: this needs to be dynamic
        this.parentSelectionMethod = ParentSelection.TOURNAMENT;
        this.survivorSelectionMethod = SurvivorSelection.REMOVE_WORST;
        this.recombinationOperator = RecombinationOperator.NR2C;

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

    public void printPopulation() {
      for(CandidateSolution sol: population){
        sol.printSolution();
      }
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

    public void evaluateChild(CandidateSolution child){
      double fitness = (double)this.evaluation.evaluate(child.getGenotype());
      if(fitness > this.bestFitness){
        this.bestFitness = fitness;
        this.bestCandidate = child;
      }
      child.setFitness(fitness);
    }

    // select parents
    // make children
    // select best 100 as survivors
    public void createNewGeneration(){
      ArrayList<CandidateSolution> newPopulation = new ArrayList<CandidateSolution>();
      for(int i=0; i< this.numberOfParentSelections; i++){
        CandidateSolution[] parents = parentSelection();
        // System.out.println("Na parentSelection");
        CandidateSolution[] children = generateChildren(parents);
        // System.out.println("Na generateChildren");
        for(CandidateSolution sol: children){
          evaluateChild(sol);
          newPopulation.add(sol);
        }
      }
      this.population = newPopulation;
    }


    public CandidateSolution[] parentSelection() {
        if(this.parentSelectionMethod == ParentSelection.RANDOM) {
            return randomParentSelection();
        } else if(this.parentSelectionMethod == ParentSelection.TOURNAMENT){
          return tournamentParentSelection();
        }
        return new CandidateSolution[]{};
    }

    public CandidateSolution[] randomParentSelection() {
      CandidateSolution[] parents = new CandidateSolution[this.numberOfParents];
      for(int i=0; i<this.numberOfParents; i++){
        parents[i] = this.population.get(randInt(0, this.populationSize-1));
      }
      return parents;
    }

    //best out of k random
    public CandidateSolution[] tournamentParentSelection() {
      CandidateSolution[] parents = new CandidateSolution[this.numberOfParents];
      for(int i=0; i<this.numberOfParents; i++){
        CandidateSolution winner = this.population.get(randInt(0, this.populationSize-1));
        for(int j=0; j<this.tournamentSize-1; j++){
          CandidateSolution challenger = this.population.get(randInt(0, this.populationSize-1));
          if(challenger.fitness > winner.fitness){
            winner = challenger;
          }
        }
        parents[i] = winner;
      }
      return parents;
    }

    //crossover
    public CandidateSolution[] generateChildren (CandidateSolution[] parents) {
        if(this.recombinationOperator == RecombinationOperator.NR2C) {
            return NR2C(parents);
        } else if(this.recombinationOperator == RecombinationOperator.BAG_OF_GENES) {
            return bagOfGenes(parents);
        } else if(this.recombinationOperator == RecombinationOperator.DIAGONAL) {
            return diagonal(parents);
        }
        // default is one point crossover
        return diagonal(parents);
    }

    public CandidateSolution[] onePointCrossOver (CandidateSolution[] parents) {
      CandidateSolution firstChild = crossOver(parents[0], parents[1]);
      CandidateSolution secondChild = crossOver(parents[1], parents[0]);
      firstChild.mutate();
      secondChild.mutate();
      CandidateSolution[] children = new CandidateSolution[]{firstChild, secondChild};
      return children;
    }

    public CandidateSolution[] NR2C(CandidateSolution[] parents) {
      int numberOfGenes = 10;
      CandidateSolution[] children = new CandidateSolution[parents.length];

      for(int i = 0; i < parents.length; i++) {
        int PCI = 0;
        CandidateSolution tmpChild = new CandidateSolution(this.random, -1, -1);
        while (PCI < numberOfGenes) {
          int CTI = -1;
          if(PCI != numberOfGenes-1) {
            CTI = randInt(PCI+1, numberOfGenes);
          } else {
            CTI = numberOfGenes;
          }

          int randomParent = randInt(0, parents.length-1);
          CandidateSolution parent = parents[randomParent];
          for(int j = PCI; j < CTI; j++) {
            tmpChild.genotype[j] = parent.genotype[j];
          }
          PCI = CTI;
        }
        children[i] = tmpChild;
      }
      return children;
    }

    public void shuffleArray(double[] ar){
      Random rnd = this.random;
      for (int i = ar.length - 1; i > 0; i--)
      {
        int index = rnd.nextInt(i + 1);
        double a = ar[index];
        ar[index] = ar[i];
        ar[i] = a;
      }
    }

    public CandidateSolution[] bagOfGenes(CandidateSolution[] parents) {
      int numberOfGenes = 10;
      double[][] bags = new double[numberOfGenes][parents.length];
      CandidateSolution[] children = new CandidateSolution[parents.length];
      //make children
      for (int g = 0; g<parents.length; g++){
        children[g] = new CandidateSolution(this.random, -1,-1);
      }
      //for each ith gene select all the parents, save the genes
      for (int i = 0; i<numberOfGenes; i++) {
        double[] bag = new double[parents.length];
        for (int j = 0; j< parents.length; j++) {
          CandidateSolution parent = parents[j];
          bag[j]= parent.genotype[i];
        }
        //shuffle genes and put them back into the children
        shuffleArray(bag);
        for (int k = 0; k < parents.length; k++) {
          children[k].genotype[i] = bag[k];
        }
      }
      return children;
    }

    public CandidateSolution[] diagonal(CandidateSolution[] parents) {
        // System.out.println("Begin diagonal");
        int numberOfGenes = 10;
        ArrayList<Integer> CIs = new ArrayList<Integer>();
        ArrayList<Integer> PIs = new ArrayList<Integer>();
        int PCI = 0;
        CIs.add(PCI);

        // produce empty children, random cutoff indices and parent indices.
        ArrayList<CandidateSolution> children = new ArrayList<CandidateSolution>();
        for (int i = 0; i < parents.length; i++) {
            while (PCI < numberOfGenes-1) {
                CIs.add(randInt(PCI+1, numberOfGenes-1));
                PCI = CIs.get(CIs.size()-1);
                PIs.add(randInt(0, parents.length-1));
                children.add(new CandidateSolution(this.random, -1, -1));
            }
            PIs.add(randInt(0, parents.length-1));
            children.add(new CandidateSolution(this.random, -1, -1));
            break;
        }

        // System.out.println("begin diagonal crossover");
        // Do the diagonal crossover
        for (int child = 0; child < children.size(); child++) {
            for (int index = 0; index < PIs.size(); index++) {  // PIs en CIs is same size
                int converted_I;
                if (index + child < PIs.size()) {
                    converted_I = index + child;
                } else {
                    converted_I = index + child - PIs.size();
                }
                children.get(child).genotype[CIs.get(converted_I)] = parents[PIs.get(converted_I)].genotype[CIs.get(converted_I)];
            }
        }

        // Get to result format of other methods
        CandidateSolution[] final_children = new CandidateSolution[children.size()];
        for (int l = 0; l < final_children.length; l++) final_children[l] = children.get(l);
        return final_children;
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
            this.population.subList(this.population.size() - this.numberOfParents*2, this.population.size()).clear();
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

import java.util.Random;
import java.util.Properties;
import java.util.ArrayList;

import org.vu.contest.ContestEvaluation;

public class Population {
    ArrayList<CandidateSolution> population;
    ContestEvaluation evaluation;
    int populationSize;
    int offSpringCount;
    double bestFitness;
    CandidateSolution bestCandidate;
    Random random;

    Population(int populationSize, int offSpringCount, ContestEvaluation evaluation, Random random){
        this.population = new ArrayList<CandidateSolution>();
        this.evaluation = evaluation;
        this.populationSize = populationSize;
        this.offSpringCount = offSpringCount;
        this.bestCandidate = null;
        this.bestFitness = -9000;
        this.random = random;
        init();
    }

    public void init (){
        for (int p = 0; p < this.populationSize; p++) {
            CandidateSolution tmpSolution = new CandidateSolution(this.random);
            this.population.add(tmpSolution);
        }
        evalutePopulation();
    }

    public void evalutePopulation(){
      for(CandidateSolution sol : this.population){
        double fitness = (double)this.evaluation.evaluate(sol.getGenotype());
        if(fitness > this.bestFitness){
          this.bestFitness = fitness;
          this.bestCandidate = sol;
        }
        sol.setFitness(fitness);
      }
    }

    public void evaluateChildren(CandidateSolution[] children){
      for(CandidateSolution sol : children){
        double fitness = (double)this.evaluation.evaluate(sol.getGenotype());
        if(fitness > this.bestFitness){
          this.bestFitness = fitness;
          this.bestCandidate = sol;
        }
        sol.setFitness(fitness);
      }
    }

    public void generation(){
      CandidateSolution[] parents = parentSelection();
      CandidateSolution[] children = generateChildren(parents);
      evaluateChildren(children);
      survivorSelection(children);
      System.out.println(this.bestFitness);
      System.out.println(this.bestCandidate.toString());
    }

    //best 2 out for random 5
    public CandidateSolution[] parentSelection () {
      CandidateSolution[] randomFive = new CandidateSolution[5];
      for(int i=0; i<5; i++){
        randomFive[i] = this.population.get(this.random.nextInt(this.populationSize));
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
      CandidateSolution child = new CandidateSolution(this.random);
      child.setGenotype(concatenateArrays(c1.getHead(), c2.getTail()));
      return child;
    }

    // remove two worst!
    public void survivorSelection (CandidateSolution[] children) {
      double worstFitness = Double.MAX_VALUE;
      CandidateSolution worst = null;

      double secondWorstFitness = Double.MAX_VALUE;
      CandidateSolution secondWorst = null;
      for(CandidateSolution sol : this.population){
        if(sol.getFitness() < worstFitness){
          secondWorstFitness = worstFitness;
          secondWorst = worst;

          worstFitness = sol.getFitness();
          worst = sol;
        }else if(sol.getFitness() < secondWorstFitness){
          secondWorstFitness = sol.getFitness();
          secondWorst = sol;
        }
      }
      this.population.remove(worst);
      this.population.remove(secondWorst);
      this.population.add(children[0]);
      this.population.add(children[1]);
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






}

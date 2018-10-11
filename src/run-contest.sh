
#!/usr/bin/env bash

# L'Originale

javac -cp contest.jar player45.java Population.java CandidateSolution.java ParentSelection.java SurvivorSelection.java RecombinationOperator.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class ParentSelection.class SurvivorSelection.class RecombinationOperator.class
java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=1
java -jar testrun.jar -submission=player45 -evaluation=SphereEvaluation -seed=1
java -jar testrun.jar -submission=player45 -evaluation=SchaffersEvaluation -seed=1
java -jar testrun.jar -submission=player45 -evaluation=KatsuuraEvaluation -seed=1

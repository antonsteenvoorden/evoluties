
#!/usr/bin/env bash

javac -cp contest.jar player45.java Population.java CandidateSolution.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class
java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=1
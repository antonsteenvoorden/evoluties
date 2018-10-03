
#!/usr/bin/env bash

javac -cp contest.jar player45.java Population.java CandidateSolution.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class
java -Dpop_size=10.0 -Dn_par=2.0 -Dm_chance=1.4 -Dgs_dev=3.0 -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=1

#-Dvar1=0.5
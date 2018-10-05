#!/usr/bin/env bash

javac -cp contest.jar player45.java Population.java CandidateSolution.java ParentSelection.java SurvivorSelection.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class ParentSelection.class SurvivorSelection.class


java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=1
#-Dpop_size=100.0 -Dn_par=5.0 -Dm_chance=0.1 -Dgs_dev=1.0



# MOET NOG
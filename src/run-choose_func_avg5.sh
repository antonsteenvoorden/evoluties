#!/bin/bash

javac -cp contest.jar player45.java Population.java CandidateSolution.java ParentSelection.java SurvivorSelection.java RecombinationOperator.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class ParentSelection.class SurvivorSelection.class RecombinationOperator.class

string=$1
string=`echo "${string,,}"`  #make lowercase

if [ "$string" = "bent" ]; then
    echo "Bent Cigar"
    total_score=0.0
    for i in {1..5}
    do
      var="$(java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=${i})"
      vars=( ${var} )
      total_score=`echo ${total_score} + ${vars[1]} | bc`
    done
    total_score=`echo ${total_score} / ${i} | bc -l`
    echo ${total_score}
fi

if [ "$string" = "Sphere" ]; then
  echo "Sphere"
  total_score=0.0
  for i in {1..5}
  do
    var="$(java -jar testrun.jar -submission=player45 -evaluation=SphereEvaluation -seed=${i})"
    vars=( ${var} )
    total_score=`echo ${total_score} + ${vars[1]} | bc`
  done
  total_score=`echo ${total_score} / ${i} | bc -l`
  echo ${total_score}
fi

if [ "$string" = "Schaffers" ]; then
  echo "Schaffers"
  total_score=0.0
  for i in {1..5}
  do
    var="$(java -jar testrun.jar -submission=player45 -evaluation=SchaffersEvaluation -seed=${i})"
    vars=( ${var} )
    total_score=`echo ${total_score} + ${vars[1]} | bc`
  done
  total_score=`echo ${total_score} / ${i} | bc -l`
  echo ${total_score}
fi

if [ "$string" = 'Katsuura' ]; then
  echo "Katsuura"
  total_score=0.0
  for i in {1..5}
  do
    var="$(java -jar testrun.jar -submission=player45 -evaluation=KatsuuraEvaluation -seed=${i})"
    vars=( ${var} )
    total_score=`echo ${total_score} + ${vars[1]} | bc`
  done
  total_score=`echo ${total_score} / ${i} | bc -l`
  echo ${total_score}
fi

# There are some security restrictions due to the online contest.
# This makes it more difficult to store results.
# If you experience these problems you can place a
# “> storeresults.txt” behind the testrun and this will put all terminal output in a file.
# To investigate the effect of parameters, you can run:
# java –Dvar1=0.5 -jar testrun.jar -submission=player1 - evaluation=BentCigarFunction -seed=1
# Then, anywhere in the code your can retrieve the value of var1 with:
# Double.parseDouble(System.getProperty(“var1”));
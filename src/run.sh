
#!/bin/bash

javac -cp contest.jar player45.java CandidateSolution.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class

if [ "$1" = "Bent" ]; then
    echo "\nBent Cigar"
    java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=1
fi

if [ "$1" = "Sphere" ]; then
    echo '\nSphere'
    java -jar testrun.jar -submission=player45 -evaluation=SphereEvaluation -seed=1
fi

if [ "$1" = "Schaffers" ]; then
    echo '\nSchaffers'
    java -jar testrun.jar -submission=player45 -evaluation=SchaffersEvaluation -seed=1
fi

if [ "$1" = 'Katsuura' ]; then
    echo '\nKatsuura (run time > 10s, wait)'
    java -jar testrun.jar -submission=player45 -evaluation=KatsuuraEvaluation -seed=1
fi

# There are some security restrictions due to the online contest.
# This makes it more difficult to store results.
# If you experience these problems you can place a
# “> storeresults.txt” behind the testrun and this will put all terminal output in a file.
# To investigate the effect of parameters, you can run:
# java –Dvar1=0.5 -jar testrun.jar -submission=player1 - evaluation=BentCigarFunction -seed=1
# Then, anywhere in the code your can retrieve the value of var1 with:
# Double.parseDouble(System.getProperty(“var1”));

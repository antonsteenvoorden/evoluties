javac -cp contest.jar player45.java Population.java CandidateSolution.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class
java -jar testrun.jar -submission=player45 -evaluation=SphereEvaluation -seed=1

# There are some security restrictions due to the online contest.
# This makes it more difficult to store results.
# If you experience these problems you can place a
# “> storeresults.txt” behind the testrun and this will put all terminal output in a file.
# To investigate the effect of parameters, you can run:
# java –Dvar1=0.5 -jar testrun.jar -submission=player1 - evaluation=BentCigarFunction -seed=1
# Then, anywhere in the code your can retrieve the value of var1 with:
# Double.parseDouble(System.getProperty(“var1”));
javac -cp contest.jar player45.java
jar cmf MainClass.txt submission.jar player45.class
java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=1
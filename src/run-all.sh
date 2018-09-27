javac -cp contest.jar player45.java CandidateSolution.java Matrix.java CholeskyDecomposition.java EigenvalueDecomposition.java LUDecomposition.java Maths.java QRDecomposition.java SingularValueDecomposition.java
jar cmf MainClass.txt submission.jar player45.class CandidateSolution.class Matrix.class CholeskyDecomposition.class EigenvalueDecomposition.class LUDecomposition.class Maths.class QRDecomposition.class SingularValueDecomposition.class
echo 'Running all evaluations..'
echo 'Bent Cigar'
java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=1
echo 'Sphere'
java -jar testrun.jar -submission=player45 -evaluation=SphereEvaluation -seed=1
echo 'Schaffers'
java -jar testrun.jar -submission=player45 -evaluation=SchaffersEvaluation -seed=1
echo 'Katsuura (run time > 10s, wait)'
java -jar testrun.jar -submission=player45 -evaluation=KatsuuraEvaluation -seed=1

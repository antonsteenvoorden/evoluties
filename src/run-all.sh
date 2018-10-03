
#!/usr/bin/env bash

javac -cp contest.jar player45.java Population.java CandidateSolution.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class
echo 'Running all evaluations..'


echo 'Bent Cigar'
total_score_Bent=0.0
for i in {1..30}
do
  var="$(java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=$i)"
  vars=( $var )
  total_score_Bent=`echo ${total_score_Bent} + ${vars[1]} | bc`
#  echo "${vars[1]}"
done
total_score_Bent=`echo ${total_score_Bent} / $i | bc -l`
echo "Average over 30 runs is... ${total_score_Bent}"


echo 'Sphere'
total_score_Sphere=0.0
for i in {1..30}
do
  var="$(java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=$i)"
  vars=( $var )
  total_score_Sphere=`echo ${total_score_Sphere} + ${vars[1]} | bc`
#  echo "${vars[1]}"
done
total_score_Sphere=`echo ${total_score_Sphere} / $i | bc -l`
echo "Average over 30 runs is... ${total_score_Sphere}"


echo 'Schaffers'
total_score_Schaffers=0.0
for i in {1..30}
do
  var="$(java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=$i)"
  vars=( $var )
  total_score_Schaffers=`echo ${total_score_Schaffers} + ${vars[1]} | bc`
#  echo "${vars[1]}"
done
total_score_Schaffers=`echo ${total_score_Schaffers} / $i | bc -l`
echo "Average over 30 runs is... ${total_score_Schaffers}"


echo 'Katsuura (run time > 30*10s = 5min, wait)'
total_score_Katsuura=0.0
for i in {1..30}
do
  var="$(java -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=$i)"
  vars=( $var )
  total_score_Katsuura=`echo ${total_score_Katsuura} + ${vars[1]} | bc`
#  echo "${vars[1]}"
done
total_score_Katsuura=`echo ${total_score_Katsuura} / $i | bc -l`
echo "Average over 30 runs is... ${total_score_Katsuura}"


robustness=0.0
robustness=`echo ${total_score_Bent} + ${total_score_Katsuura} + ${total_score_Schaffers} + ${total_score_Sphere} | bc`
robustness=`echo ${robustness} / 4 | bc -l`
echo "Robustness: average score over all 4 functions is ${robustness}"
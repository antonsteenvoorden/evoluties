#!/usr/bin/env bash

#Running example
# bash run_all-choose_params+recomb.sh -pop 10 -n 1 -m 1 -gs 1 -ps 0 -ss 0 -ro 0
# population, number of parents, mutation rate, gaussian standard deviation, parent selection, survival selection, recombination operator
# -ps,-ss,-ro are defined as in their specific classes (so 0 is the first etc.)

javac -cp contest.jar player45.java Population.java CandidateSolution.java ParentSelection.java SurvivorSelection.java RecombinationOperator.java
jar cmf MainClass.txt submission.jar player45.class Population.class CandidateSolution.class ParentSelection.class SurvivorSelection.class RecombinationOperator.class

echo 'Running all evaluations..'
for ((i=1;i<=$#;i++));
do
    if [ ${!i} = "-pop" ]
    then ((i++))
        pop_size=${!i};

    elif [ ${!i} = "-n" ];
    then ((i++))
        n_par=${!i};

    elif [ ${!i} = "-m" ];
    then ((i++))
        m_chance=${!i};

    elif [ ${!i} = "-gs" ];
    then ((i++))
        gs_dev=${!i};

    elif [ ${!i} = "-ps" ];
    then ((i++))
        par_sel=${!i};

    elif [ ${!i} = "-ss" ];
    then ((i++))
        sur_sel=${!i};

    elif [ ${!i} = "-ro" ];
    then ((i++))
        rec_ope=${!i};
    fi
done;
#
# echo 'Bent Cigar'
# total_score_Bent=0.0
# for i in {1..30}
# do
#   var="$(java -Dpop_size=$pop_size -Dn_par=$n_par -Dm_chance=$m_chance -Dgs_dev=$gs_dev -Dpar_sel=$par_sel -Dsur_sel=$sur_sel -Drec_ope=$rec_ope -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=$i)"
#   vars=( $var )
# #  echo ${vars}
#   total_score_Bent=`echo ${total_score_Bent} + ${vars[-3]} | bc`
# #  echo ${total_score_Bent}
#  echo "${vars[-3]}"
# done
# total_score_Bent=`echo ${total_score_Bent} / $i | bc -l`
# echo "Average over 30 runs is... ${total_score_Bent}"
#
# echo 'Sphere'
# total_score_Sphere=0.0
# for i in {1..30}
# do
#   var="$(java -Dpop_size=$pop_size -Dn_par=$n_par -Dm_chance=$m_chance -Dgs_dev=$gs_dev -Dpar_sel=$par_sel -Dsur_sel=$sur_sel -Drec_ope=$rec_ope -jar testrun.jar -submission=player45 -evaluation=SphereEvaluation -seed=$i)"
#   vars=( $var )
#   total_score_Sphere=`echo ${total_score_Sphere} + ${vars[-3]} | bc`
#   echo "${vars[-3]}"
# done
# total_score_Sphere=`echo ${total_score_Sphere} / $i | bc -l`
# echo "Average over 30 runs is... ${total_score_Sphere}"


echo 'Schaffers'
total_score_Schaffers=0.0
for i in {1..30}
do
  var="$(java -Dpop_size=$pop_size -Dn_par=$n_par -Dm_chance=$m_chance -Dgs_dev=$gs_dev -Dpar_sel=$par_sel -Dsur_sel=$sur_sel -Drec_ope=$rec_ope -jar testrun.jar -submission=player45 -evaluation=SchaffersEvaluation -seed=$i)"
  vars=( $var )
  total_score_Schaffers=`echo ${total_score_Schaffers} + ${vars[-3]} | bc`
  echo "${vars[-3]]}"
done
total_score_Schaffers=`echo ${total_score_Schaffers} / $i | bc -l`
echo "Average over 30 runs is... ${total_score_Schaffers}"


echo 'Katsuura (run time > 30*10s = 5min, wait)'
total_score_Katsuura=0.0
for i in {1..30}
do
  var="$(java -Dpop_size=$pop_size -Dn_par=$n_par -Dm_chance=$m_chance -Dgs_dev=$gs_dev -Dpar_sel=$par_sel -Dsur_sel=$sur_sel -Drec_ope=$rec_ope -jar testrun.jar -submission=player45 -evaluation=KatsuuraEvaluation -seed=$i)"
  vars=( $var )
  total_score_Katsuura=`echo ${total_score_Katsuura} + ${vars[-3]} | bc`
  echo "${vars[-3]}"
done
total_score_Katsuura=`echo ${total_score_Katsuura} / $i | bc -l`
echo "Average over 30 runs is... ${total_score_Katsuura}"


robustness=0.0
robustness=`echo ${total_score_Bent} + ${total_score_Katsuura} + ${total_score_Schaffers} + ${total_score_Sphere} | bc`
robustness=`echo ${robustness} / 4 | bc -l`
echo "Robustness: average score over all 4 functions is ${robustness}"

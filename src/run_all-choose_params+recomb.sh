#!/usr/bin/env bash
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

echo "$pop_size";
echo "$n_par";
echo "$m_chance";
echo "$gs_dev";

echo 'Bent Cigar'
total_score_Bent=0.0
for i in {1..30}
do
  var="$(java -Dpop_size=$pop_size -Dn_par=$n_par -Dm_chance=$m_chance -Dgs_dev=$gs_dev -Dpar_sel=$par_sel -Dsur_sel=$sur_sel -Drec_ope=$rec_ope -jar testrun.jar -submission=player45 -evaluation=BentCigarFunction -seed=$i)"
  vars=( $var )
  total_score_Bent=`echo ${total_score_Bent} + ${vars[1]} | bc`
#  echo "${vars[1]}"
done
total_score_Bent=`echo ${total_score_Bent} / $i | bc -l`
echo "Average over 30 runs is... ${total_score_Bent}"
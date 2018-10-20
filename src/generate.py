import subprocess
#5, 7, 8, 9
num_parents = [1,2,3,4,5,6,7,8,9]

for i in num_parents:
    result = subprocess.run(["./run_all-choose_params+recomb.sh", "-pop", "100", "-n", "{}".format(i), "-m", "1.0", "-gs_dev", "0.1", "-ps", "2", "-ss", "0", "-ro", "0"], stdout=subprocess.PIPE)
    with open("results_short/output_nr2c_schaffers_{}.txt".format(i), "w") as f:
        f.write(result.stdout.decode("utf-8") )

    #result = subprocess.run(["./run_all-choose_params+recomb.sh", "-pop", "100", "-n", "{}".format(i), "-m", "1.0", "-gs_dev", "0.1", "-ps", "2", "-ss", "0", "-ro", "1"], stdout=subprocess.PIPE)
    #with open("results_short/output_diag_schaffers_{}.txt".format(i), "w") as f:
    #    f.write(result.stdout.decode("utf-8") )

    #result = subprocess.run(["./run_all-choose_params+recomb.sh", "-pop", "100", "-n", "{}".format(i), "-m", "1.0", "-gs_dev", "0.1", "-ps", "2", "-ss", "0", "-ro", "2"], stdout=subprocess.PIPE)
    #with open("results_short/output_bog_schaffers_{}.txt".format(i), "w") as f:
    #    f.write(result.stdout.decode("utf-8") )

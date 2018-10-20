import pandas as pd
import numpy as np
import scipy
from scipy.spatial import distance
num_generations = 1000
population_size = 100

implementations = ["nr2c", "diag", "bog"]
num_parents = ["1", "2", "3", "4", "5", "6", "7", "8", "9"]

for imp in implementations:
  for n_p in num_parents:
    print("- "+imp+", -"+n_p)
    #lets make katsuura nr2c
    mean_fitness_values = []
    mean_diversity_values = []

    with open("results/output_"+imp+"_schaffers_"+n_p+".txt") as f:
      f.readline()
      f.readline()
      for k in range(0,30):
        fitness_values = []
        diversity_values = []
        for i in range(0,100):
          generation = pd.DataFrame()
          for j in range(0,population_size):
            line = f.readline().split(',')
            for l in range(0,10):
              line[l] = float(line[l])
            line[10] = float(line[10][:-1])
            serie = pd.Series(line)
            generation = generation.append(serie,ignore_index=True)
          #we now have a dataframe of a generation
          if (i+1) % 10 == 0:
            fitness_values.append(generation[generation.columns[10]].mean())
          
          distances = []
          mean_vector = []
          for j in range(0,10):
            mean_vector.append(generation[generation.columns[j]].mean())
          mean_vector = np.array(mean_vector)
          for j in range(0,population_size):
            distances.append(scipy.spatial.distance.euclidean(mean_vector, generation.iloc[j,0:10].values))
          diversity_values.append(sum(distances)/len(distances))

        for i in range(0,900):
          if (i+1) % 10 == 0:
            generation = pd.DataFrame()
            for j in range(0, population_size):
              line = f.readline().split(',')
              for l in range(0,10):
                line[l] = float(line[l])
              line[10] = float(line[10][:-1])
              serie = pd.Series(line)
              generation = generation.append(serie,ignore_index=True)
            fitness_values.append(generation[generation.columns[10]].mean())
          else:
            for j in range(0, population_size):
              f.readline()
        score = float(f.readline().split(" ")[1][:-1])
        f.readline()
        fitness_values.append(score)
        mean_fitness_values.append(fitness_values)
        mean_diversity_values.append(diversity_values)
    fitness_dataframe = pd.DataFrame(mean_fitness_values)
    diversity_dataframe = pd.DataFrame(mean_diversity_values)
    fitness_dataframe.to_csv("results/parsed/fitness_schaffers_"+imp+"_"+n_p+".csv")
    diversity_dataframe.to_csv("results/parsed/diversity_schaffers_"+imp+"_"+n_p+".csv")

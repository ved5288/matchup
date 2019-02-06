## SEA-2019

---

### SEAT - Data Generator & Max. Card. Matching using CPLEX

*_Software Dependencies_* - python 3.5, numpy

Run the following terminal command to generate a SEAT instance:

`python3 generate_instance_seat.py <n1> <n2> <k_low> <k_high> <flag> <outputfolder>`

Where n1 is the number of residents/students, n2 is the number of hospitals/courses, k_low & k_up are the range for length of resident/student preference lists and outputfolder is the folder to which the instance is generated. 

Note that when flag=0, the instance generated corresponds to the master model, where the preference lists of students and courses are ordered based on a master list, and when flag=1, the instance generated corresponds to the shuffle model, where the preference lists are ordered randomly.

---

Run the following command to generate the max. cardinality LP file for an instance and solve the LP using CPLEX solver (must be separately installed from IBM website).

*_Software Dependencies_* - IBM ILOG CPLEX Optimization Studio v12.8 - python 3.5 API


`python3 run_cplex.py <input_dir>`

Where input_dir corresponds to the folder containing the instance. On execution it automatically generates max_card_lp.txt file into the same directory and then invokes CPLEX to solve the LP and its output matching obtained is written to input_dir/output.csv file.


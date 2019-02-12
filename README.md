## SEA-2019

### SEAT - Input Data Generator 

(_Directory :_  `/code/input_generator/`)

*_Software Dependencies_* - python 3.5, numpy

Run the following terminal command to generate a SEAT instance:

`python3 generate_instance_seat.py <n1> <n2> <k_low> <k_high> <flag> <output_folder>`

Where `n1` is the number of residents/students, `n2` is the number of hospitals/courses, `k_low` & `k_up` are the range for length of resident/student preference lists and `output_folder` is the folder to which the instance is generated. 

Note that when `flag = 0`, the instance generated corresponds to the master model, where the preference lists of students and courses are ordered based on a master list, and when `flag = 1`, the instance generated corresponds to the shuffle model, where the preference lists are ordered randomly.

---

### SEAT - Compute Maximum Cardinality Matching using CPLEX

*_Software Dependencies_* - IBM ILOG CPLEX Optimization Studio v12.8 - python 3.5 API

Run the following command to generate the max. cardinality LP file for an instance and solve the LP using CPLEX solver.

`python3 run_cplex.py <input_dir>`

Where `input_dir` corresponds to the folder containing the instance. On execution it automatically generates `max_card_lp.txt` file into the same directory and then invokes CPLEX to solve the LP and its output matching obtained is written to `input_dir/output.csv` file.

---

### SEAT - Compiling the Code

Do the following

`cd code`

`make`

This would create a `SEAT.jar` in the the _code_ directory.

---

### SEAT - Computing the matching using FirstPreference and IterativeHR algorithms

Run the following command

`java -jar SEAT.jar`

It will ask for the following input:

`Please enter the location of file where the list of students is stored` : _Input the studentList.csv's location_

`Please enter the location of file where the list of courses and their respective capacities are stored` : _Input the courseList.csv's location_

`Please enter the location of file where student preferences are stored` : _Input the studentPreferenceList.csv's location_

`Please enter the location of file where course preferences are stored` : _Input the coursePreferenceList.csv's location_

`Please enter the location of file from where you want the master class constraint specifications to be read:` : _Input the masterClassSpecification.csv's location_

`Please enter the location of file from where you want the student class constraint specifications to be read:` : _Input the studentClassSpecification.csv's location_

`Which algorithm to run?`

`1. Iterative HR`

`2. First Preference Allotment`

`3. Load Max Cardinality Matching` : _Enter 1 for iterativeHR, and 2 for firstPreference_

`Please enter output folder to print to` : _Enter the directoryname in which you want the output to be stored_


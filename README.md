## SEA-2019

### Software Dependencies & Code Compilation
python 3.5, numpy, IBM ILOG CPLEX Optimization Studio v12.8 - python 3.5 API, java

To compile the java code, do the following

`cd code` <br />
`make`

This would create a `CourseAllocationTool.jar` in the _code_ directory

---

### Input Data Generator (`/code/input_generator/`)

Run the following terminal command to randomly generate an instance with command line arguments:

`python3 generate_instance.py <n1> <n2> <k_low> <k_high> <flag> <output_folder>`

Where `n1` is the number of residents/students, `n2` is the number of hospitals/courses, `k_low` & `k_up` are the range for length of resident/student preference lists and `output_folder` is the folder to which the instance is generated. 

Note that when `flag = 0`, the instance generated corresponds to the master model, where the preference lists of students and courses are ordered based on a master list, and when `flag = 1`, the instance generated corresponds to the shuffle model, where the preference lists are ordered randomly.

---

### Computing Maximum Cardinality Matching using CPLEX

Run the following command to generate the maximum cardinality LP file for an instance and solve the LP using CPLEX solver.

`python3 run_cplex.py <input_dir>`

Where `input_dir` corresponds to the folder containing the instance. On execution it automatically generates `max_card_lp.txt` file into the same directory and then invokes CPLEX to solve the LP and its output matching obtained is written to `input_dir/output.csv` file.

---

### Computing matching & statistics using FP+IAF and GS+IAF algorithms

Run the following command

`java -jar CourseAllocationTool.jar`

It will ask for the following input:

`Please enter the location of file where the list of students is stored` : _Input the studentList.csv's location_

`Please enter the location of file where the list of courses and their respective capacities are stored` : _Input the courseList.csv's location_

`Please enter the location of file where student preferences are stored` : _Input the studentPreferenceList.csv's location_

`Please enter the location of file where course preferences are stored` : _Input the coursePreferenceList.csv's location_

`Please enter the location of file from where you want the master class constraint specifications to be read` : _Input the masterClassSpecification.csv's location_

`Please enter the location of file from where you want the student class constraint specifications to be read` : _Input the studentClassSpecification.csv's location_

`Which algorithm to run?` <br />
`1. Gale-Shapley + Iterative Allotment Framework` <br />
`2. First Preference + Iterative Allotment Framework` <br />
`3. Load Max Cardinality Matching`

_Enter 1 for Gale-Shapley + IAF, and 2 for First Preference + IAF and 3 for loading max cardinality matching_

Additionally, if _3_ is chosen, the following will be prompted:

`Please enter the location of file from where you want the output to be read` : _Input the output.csv's location_

`Please enter output folder to print to` : _Enter the directory name in which you want the output to be stored_

The output folder finally contains the resultant matching along with other statistics such as MEAR, exchange blocking pairs, blocking pairs, etc.

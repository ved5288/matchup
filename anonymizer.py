#!/bin/python

import sys
import csv
import random
import os.path

if (len(sys.argv)!=3):
    print "Arg1: Input Directory path"
    print "Arg2: Output Directory path"
    print "Usage: python anonymizer.py <path-to-directory>"
    sys.exit(0)

inpdirectory = sys.argv[1]
if inpdirectory[-1] != '/':
	inpdirectory += "/"

outdirectory = sys.argv[2]
if outdirectory[-1] != '/':
	outdirectory += "/"

departmentMapping = []
batchMapping = []
rollNumberMapping = []
courseNumberMapping = []
slotMapping = []

def generateTwoLengthAlphaBet():
	char1 = chr(ord('A')+random.randint(0,25))
	char2 = chr(ord('A')+random.randint(0,25))

	retString = ""+char1+char2

	return retString

def findRollNumberCode(inpRollNumber):

	if inpRollNumber.rstrip() == "":
		return ""

	for mapping in rollNumberMapping:
		if mapping[0] == inpRollNumber:
			return mapping[1]

def findCourseNumberCode(inpCourseNumber):

	if inpCourseNumber.rstrip() == "":
		return ""

	for mapping in courseNumberMapping:
		if mapping[0] == inpCourseNumber:
			return mapping[1]

def findBatchCode(inpBatch):

	if inpBatch.rstrip() == "":
		return ""

	for mapping in batchMapping:
		if mapping[0] == inpBatch:
			return mapping[1]

	print inpBatch+" does not exist. Correct the input files. Exiting"
	sys.exit(1)

def findSlotCode(inpSlot):

	if inpSlot.rstrip() == "":
		return ""

	for mapping in slotMapping:
		if mapping[0] == inpSlot:
			return mapping[1]

def findDepartmentCode(inpDep):

	if inpDep.rstrip() == "":
		return ""

	for mapping in departmentMapping:
		if mapping[0] == inpDep:
			return mapping[1]

#--------------------------------------------------------------------------------------------------------------

#Check student list's existence
if os.path.isfile(inpdirectory+"studentList.csv") :
	pass
else:
	print "studentList.csv does not exist in the directory. Exiting"
	sys.exit(1)

with open(inpdirectory+"studentList.csv","r") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		if(header):
			header = 0
			continue

		rollNumber = row[0]
		department = rollNumber[0:2]

		if department not in [entry[0] for entry in departmentMapping]:
			mapping = generateTwoLengthAlphaBet()

			while(mapping in [entry[1] for entry in departmentMapping]):
				mapping = generateTwoLengthAlphaBet()

			temp = []
			temp.append(department)
			temp.append(mapping)

			departmentMapping.append(temp)

		depCode = ""
		for mapping in departmentMapping:
			if mapping[0] == department:
				depCode = mapping[1]

		batch = rollNumber[0:4]

		if batch not in [entry[0] for entry in batchMapping]:
			mapping = depCode+batch[2:4]

			temp = []
			temp.append(batch)
			temp.append(mapping)

			batchMapping.append(temp)

		batchCode = depCode+batch[2:4]
		
		tempRollNumber = batchCode + generateTwoLengthAlphaBet() + generateTwoLengthAlphaBet()

		while(tempRollNumber in [entry[1] for entry in rollNumberMapping]):
			tempRollNumber = batchCode + generateTwoLengthAlphaBet() + generateTwoLengthAlphaBet()

		temp = []
		temp.append(rollNumber)
		temp.append(tempRollNumber)
		rollNumberMapping.append(temp)

#Check course list's existence
if os.path.isfile(inpdirectory+"courseList.csv") :
	pass
else:
	print "courseList.csv does not exist in the directory. Exiting"
	sys.exit(1)

with open(inpdirectory+"courseList.csv","r") as file_obj:
	reader = csv.reader(file_obj)
	header = 1
	for row in reader:
		if header:
			header = 0
			continue

		courseNumber = row[0]
		department = courseNumber[0:2]

		if department not in [entry[0] for entry in departmentMapping]:
			mapping = generateTwoLengthAlphaBet()

			while(mapping in [entry[1] for entry in departmentMapping]):
				mapping = generateTwoLengthAlphaBet()

			temp = []
			temp.append(department)
			temp.append(mapping)

			departmentMapping.append(temp)

		depCode = ""
		for mapping in departmentMapping:
			if mapping[0] == department:
				depCode = mapping[1]

		tempCourseNumber = depCode + generateTwoLengthAlphaBet() + generateTwoLengthAlphaBet()

		while(tempCourseNumber in [entry[1] for entry in courseNumberMapping]):
			tempCourseNumber = depCode + generateTwoLengthAlphaBet() + generateTwoLengthAlphaBet()

		temp = []
		temp.append(courseNumber)
		temp.append(tempCourseNumber)
		courseNumberMapping.append(temp)

#Check slot_config's existence
if os.path.isfile(inpdirectory+"slot_config.csv") :
	pass
else:
	print "slot_config.csv does not exist in the directory. Exiting"
	sys.exit(1)

with open(inpdirectory+"slot_config.csv","r") as file_obj:
	reader = csv.reader(file_obj)
	header = 1
	for row in reader:
		if header:
			header = 0
			continue

		tempSlot = generateTwoLengthAlphaBet()
		while(tempSlot in [entry[1] for entry in slotMapping]):
			tempSlot = generateTwoLengthAlphaBet()

		temp = []
		temp.append(row[0])
		temp.append(tempSlot)
		slotMapping.append(temp)

#--------------------------------------------------------------------------------------------------------------

if os.path.exists(outdirectory):
	pass
else:
	print "Output Directory doesn't exist. Creating the directory"
	os.makedirs(outdirectory)

### Write the slot_config.csv
outfile = open(outdirectory+"slot_config.csv","w")
with open(inpdirectory+"slot_config.csv","r") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:

		if header:
			outfile.write(row[0])
		else:
			outfile.write(findSlotCode(row[0]))

		for i in range(1,len(row)):
			outfile.write(","+row[i])

		outfile.write("\n")
		header = 0

print "Created the file "+outdirectory+"slot_config.csv"
outfile.close()



### Write the studentList.csv
outfile = open(outdirectory+"studentList.csv","w")
with open(inpdirectory+"studentList.csv","r") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		
		if header:
			outfile.write(row[0]+",")
		else:
			outfile.write(findRollNumberCode(row[0])+",")

		outfile.write(row[1]+",")
		outfile.write(row[2]+"\n")

		header = 0

print "Created the file "+outdirectory+"studentList.csv"
outfile.close()

### Write the courseList.csv
outfile = open(outdirectory+"courseList.csv","w")
with open(inpdirectory+"courseList.csv") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		
		if header:
			outfile.write(row[0]+",")
		else:
			outfile.write(findCourseNumberCode(row[0])+",")

		if header:
			outfile.write(row[1]+",")
		else:
			outfile.write(findDepartmentCode(row[1])+",")

		outfile.write(row[2]+",")
		outfile.write(row[3]+",")
		outfile.write(row[4]+",")
		outfile.write(row[5]+",")

		if header:
			outfile.write(row[6])
			outfile.write(","+row[7])
		else:
			outfile.write(findSlotCode(row[6]))
			if len(row) >= 8:
				outfile.write(","+findSlotCode(row[7]))

		for i in range(8,len(row)):
			outfile.write(","+findSlotCode(row[i]))

		outfile.write("\n")

		header = 0

print "Created the file "+outdirectory+"courseList.csv"
outfile.close()


### Write the studentRegistrationData.csv

#Check student registration data file's existence
if os.path.isfile(inpdirectory+"studentRegistrationData.csv") :
	pass
else:
	print "studentRegistrationData.csv does not exist in the directory. Exiting"
	sys.exit(1)

outfile = open(outdirectory+"studentRegistrationData.csv","w")
with open(inpdirectory+"studentRegistrationData.csv") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		
		if header:
			outfile.write(row[0]+",")
			outfile.write(row[1]+",")
		else:
			outfile.write(findRollNumberCode(row[0])+",")
			outfile.write(findCourseNumberCode(row[1])+",")

		outfile.write(row[2]+",")
		outfile.write(row[3]+",")
		outfile.write(row[4]+"\n")

		header = 0

print "Created the file "+outdirectory+"studentRegistrationData.csv"
outfile.close()

### Write the highPriorityStudents.csv

#Check highPriorityStudents file's existence
if os.path.isfile(inpdirectory+"highPriorityStudents.csv") :
	pass
else:
	print "highPriorityStudents.csv does not exist in the directory. Exiting"
	sys.exit(1)

outfile = open(outdirectory+"highPriorityStudents.csv","w")
with open(inpdirectory+"highPriorityStudents.csv") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		
		if header:

			outfile.write(row[0])
			for i in range(1,len(row)):
				outfile.write(","+row[i])
			outfile.write("\n")
			header = 0

		else :

			outfile.write(findCourseNumberCode(row[0]))
			for i in range(1,len(row)):
				outfile.write(","+findBatchCode(row[i]))
			outfile.write("\n")
			header = 0

print "Created the file "+outdirectory+"highPriorityStudents.csv"
outfile.close()

### Write the insideDepartmentSpecification.csv

#Check insideDepartmentSpecification file's existence
if os.path.isfile(inpdirectory+"insideDepartmentSpecification.csv") :
	pass
else:
	print "insideDepartmentSpecification.csv does not exist in the directory. Exiting"
	sys.exit(1)

outfile = open(outdirectory+"insideDepartmentSpecification.csv","w")
with open(inpdirectory+"insideDepartmentSpecification.csv") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		
		if header:

			outfile.write(row[0])
			for i in range(1,len(row)):
				outfile.write(","+row[i])
			outfile.write("\n")
			header = 0

		else :

			outfile.write(findCourseNumberCode(row[0]))
			for i in range(1,len(row)):
				outfile.write(","+findBatchCode(row[i]))
			outfile.write("\n")
			header = 0

print "Created the file "+outdirectory+"insideDepartmentSpecification.csv"
outfile.close()

### Write the batchSpecificMandatedElectives.csv

#Check batchSpecificMandatedElective file's existence
if os.path.isfile(inpdirectory+"batchSpecificMandatedElectives.csv") :
	pass
else:
	print "batchSpecificMandatedElectives.csv does not exist in the directory. Exiting"
	sys.exit(1)

outfile = open(outdirectory+"batchSpecificMandatedElectives.csv","w")
with open(inpdirectory+"batchSpecificMandatedElectives.csv") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		
		if header:

			outfile.write(row[0])
			for i in range(1,len(row)):
				outfile.write(","+row[i])
			outfile.write("\n")
			header = 0

		else :

			outfile.write(findBatchCode(row[0]))
			for i in range(1,len(row)):
				outfile.write(","+findDepartmentCode(row[i]))
			outfile.write("\n")
			header = 0

print "Created the file "+outdirectory+"batchSpecificMandatedElectives.csv"
outfile.close()

### Write the maxCreditLimits.csv

#Check maxCreditLimit file's existence
if os.path.isfile(inpdirectory+"maxCreditLimits.csv") :
	pass
else:
	print "maxCreditLimits.csv does not exist in the directory. Exiting"
	sys.exit(1)

outfile = open(outdirectory+"maxCreditLimits.csv","w")
with open(inpdirectory+"maxCreditLimits.csv") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		
		if header:
			outfile.write(row[0]+",")
		else:
			outfile.write(findBatchCode(row[0])+",")
		outfile.write(row[1]+"\n")

		header = 0

print "Created the file "+outdirectory+"maxCreditLimits.csv"
outfile.close()

### Write the coursePreferenceList.csv

#Check coursePreferenceList file's existence
if os.path.isfile(inpdirectory+"coursePreferenceList.csv") :
	pass
else:
	print "coursePreferenceList.csv does not exist in the directory. Exiting"
	sys.exit(1)

outfile = open(outdirectory+"coursePreferenceList.csv","w")
with open(inpdirectory+"coursePreferenceList.csv") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
		
		if header:

			outfile.write(row[0])
			for i in range(1,len(row)):
				outfile.write(","+row[i])
			outfile.write("\n")
			header = 0

		else :

			course = row[0].split('$')[0]
			courseCat = row[0].split('$')[1]

			outfile.write(findCourseNumberCode(course)+"$"+courseCat)
			for i in range(1,len(row)):
				outfile.write(","+findRollNumberCode(row[i]))
			outfile.write("\n")
			header = 0

print "Created the file "+outdirectory+"coursePreferenceList.csv"
outfile.close()

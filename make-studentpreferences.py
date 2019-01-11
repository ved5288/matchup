import csv
import sys
import numpy as np 

studentRegFile = sys.argv[1]
outputFile = sys.argv[2]

colorClasses = []

currentStudentRoll = ""
currentClassCourses = []
currentColorGroup = -1

def dump(studentRoll,courses):
	temp = []
	temp.append(studentRoll)
	temp.append(courses)
	colorClasses.append(temp)

	currentStudentRoll = ""
	currentClassCourses = []
	currentColorGroup = -1





with open(studentRegFile,"r") as file_obj:
	reader = csv.reader(file_obj)
	header = 1

	for row in reader:
			if(currentStudentRoll != "")
		if header:
			header = 0
			continue

		studentRoll = row[0]
		course = row[1]
		colorGroup = int(row[2])

		if(studentRoll != currentStudentRoll):
			# student has changed. Dump into the array
			dump(currentStudentRoll, currentClassCourses)
			currentStudentRoll = studentRoll
			


		studentIndex = doesStudentExist(studentRoll)

		if(studentIndex != -1):
			students[studentIndex][1].append(row[1])
		else:
			temp = []
			temp.append(row[0])
			temp1 = []
			temp1.append(row[1])
			temp.append(temp1)

			students.append(temp)

ofile = open(outputFile,"w")
ofile.write("RollNo,Preferences\n")
for student in students:
	ofile.write(student[0])
	for course in student[1]:
		ofile.write(","+course)
	ofile.write("\n")
ofile.close()


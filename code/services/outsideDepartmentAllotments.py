import csv
import argparse

parser = argparse.ArgumentParser()

parser.add_argument('--insideSpec', help="Inside Department Specification")
parser.add_argument('--output', help="The allotment file")

args = parser.parse_args()

insideSpecFile = args.insideSpec #"/home/vedant/seatv0.2/28NovAllotment1500PM/registrationDataWithBS17CS16ME16changes.csv"
allotmentFile = args.output

insideDepartmentSpecs = []

with open(insideSpecFile,"r") as file_obj:
	reader = csv.reader(file_obj)
	for row in reader:
		if(row[0]!="Course"):
			insideDepartmentSpecs.append(row)

insideDepartmentAllotment = 0
outsideDepartmentAllotment = 0

with open(allotmentFile,"r") as file_obj:
	reader = csv.reader(file_obj)
	for row in reader:
		if(row[0]!="Student Roll Number"):
			studentBatch = row[0][0:4]
			flag = 0
			for course in insideDepartmentSpecs:
				if(course[0]==row[1]):

					for i in range(1,len(course)):
						if(studentBatch==course[i]):
							insideDepartmentAllotment = insideDepartmentAllotment + 1
							# print row[0]+","+row[1]+",inside"
							flag = 1
							break
				if(flag==1):
					break




			if(flag==0):

				if(studentBatch[0:2]==row[1][0:2]):
						insideDepartmentAllotment = insideDepartmentAllotment + 1
						# print row[0]+","+row[1]+",inside"
						flag = 1
				
				if(flag==0):	
					outsideDepartmentAllotment = outsideDepartmentAllotment + 1
					# print row[0]+","+row[1]+",outside"

print "Outside Department Allotments : ",outsideDepartmentAllotment
print "Inside Department Allotments : ",insideDepartmentAllotment





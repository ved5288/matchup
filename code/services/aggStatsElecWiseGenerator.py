import csv
import argparse

parser = argparse.ArgumentParser()

parser.add_argument('--reg', help="Student Registration Data")
parser.add_argument('--saveDir', help="Save in this directory")
parser.add_argument('--output', help="The allotment file")

args = parser.parse_args()

registrationFile = args.reg #"/home/vedant/seatv0.2/28NovAllotment1500PM/registrationDataWithBS17CS16ME16changes.csv"
allotmentFile = args.output
saveDir = args.saveDir

onlyHS = []
onlyMA = []
others = []
students = []

with open(registrationFile,"r") as file_obj:
	reader = csv.reader(file_obj)
	for row in reader:
		if(row[0]!="RollNo"):

			student = row[0]
			if(student not in students):
				students.append(student)
			if(row[3]=="ELEC"):
				if(row[1][0:2]=="HS"):
					onlyHS.append(row)
				elif(row[1][0:2]=="MA"):
					onlyMA.append(row)
				else:
					others.append(row)

# Get the list of students who gave preferences
listOfStudentsHS = []
listOfStudentsMA = []
listOfStudentsOthers = []

for preference in onlyHS:
	student = preference[0]
	if student in listOfStudentsHS:
		pass
	else:
		listOfStudentsHS.append(student)

for preference in onlyMA:
	student = preference[0]
	if student in listOfStudentsMA:
		pass
	else:
		listOfStudentsMA.append(student)


for preference in others:
	student = preference[0]
	if student in listOfStudentsOthers:
		pass
	else:
		listOfStudentsOthers.append(student)


print "Number of students who gave preferences for HS:",len(listOfStudentsHS)
print "Number of students who gave preferences for MA:",len(listOfStudentsMA)
print "Number of students who gave preferences for others:",len(listOfStudentsOthers)

# For a student, reorder the preferences
for student in listOfStudentsHS:
	number = 1
	for i in range(len(onlyHS)):
		preference = onlyHS[i]
		if(preference[0]==student):
			onlyHS[i][4] = number
			number += 1

for student in listOfStudentsMA:
	number = 1
	for i in range(len(onlyMA)):
		preference = onlyMA[i]
		if(preference[0]==student):
			onlyMA[i][4] = number
			number += 1



# file_obj = open(args.saveDir+"HSprojectedPreferences.csv","w")
# file_obj.write("RollNo,CourseNo,ColorGroup,Type,SortOrder\n")
# for preference in onlyHS:
# 	file_obj.write(preference[0]+","+preference[1]+","+preference[2]+","+preference[3]+","+str(preference[4])+"\n")
# file_obj.close()

# file_obj = open(args.saveDir+"MAprojectedPreferences.csv","w")
# file_obj.write("RollNo,CourseNo,ColorGroup,Type,SortOrder\n")
# for preference in onlyMA:
# 	file_obj.write(preference[0]+","+preference[1]+","+preference[2]+","+preference[3]+","+str(preference[4])+"\n")
# file_obj.close()

for student in listOfStudentsOthers:
	number = 1
	for i in range(len(others)):
		preference = others[i]
		if(preference[0]==student):
			others[i][4] = number
			number += 1




allotments = []
# allotmentFile = "/home/vedant/seatv0.2/28NovAllotment1500PM/firstPreferenceAllotment/output.csv"
with open(allotmentFile,"r") as file_obj:
	reader = csv.reader(file_obj)
	for row in reader:
		if(row[0]!="RollNumber"):
			allotments.append(row)


def isAllotted(student, course):

	for allotment in allotments:
		if(allotment[0]==student):
			if(allotment[1]==course):
				return 1
	return 0


aggHS = [[] for x in range(20)]
aggMA = [0 for x in range(20)]
aggOthers = [[] for x in range(20)]

lastHS = 0
lastMA = 0
lastOthers = 0

HSallottedPreference = []
for student in listOfStudentsHS:
	allottedPreference = 0
	for preference in onlyHS:
		if(preference[0]==student):
			if(isAllotted(student,preference[1])==1):
				allottedPreference = preference[4]
				aggHS[preference[4]+1].append(student)
				if(preference[4]+1 > lastHS):
					lastHS = preference[4]+1
				break
	temp = []
	temp.append(student)
	if(allottedPreference!=0):
		temp.append(str(allottedPreference))
	else:
		temp.append("No Course Allotted")
		aggHS[1].append(student)
		if(lastHS<1):
			lastHS = 1
	HSallottedPreference.append(temp)


MAallottedPreference = []
for student in listOfStudentsMA:
	allottedPreference = 0
	for preference in onlyMA:
		if(preference[0]==student):
			if(isAllotted(student,preference[1])==1):
				allottedPreference = preference[4]
				aggMA[preference[4]+1] += 1
				if(preference[4]+1 > lastMA):
					lastMA = preference[4]+1
				break
	temp = []
	temp.append(student)
	if(allottedPreference!=0):
		temp.append(str(allottedPreference))
	else:
		temp.append("No Course Allotted")
		aggMA[1] += 1
		if(lastMA<1):
			lastMA = 1
	MAallottedPreference.append(temp)


OtherallottedPreference = []
for student in listOfStudentsOthers:
	sum_ = 0
	count = 0
	allottedPreference = "No Course Allotted"
	for preference in others:
		if(preference[0]==student):
			if(isAllotted(student,preference[1])==1):
				sum_ += preference[4]
				if(count==0):
					allottedPreference = str(preference[4])
					aggOthers[preference[4]+1].append(student)
					if(preference[4]+1 > lastOthers):
						lastOthers = preference[4]+1

				else:
					allottedPreference += ","+str(preference[4])
					aggOthers[preference[4]+1].append(student)
					if(preference[4]+1 > lastOthers):
						lastOthers = preference[4]+1

				count += 1

	if(count == 0):
		aggOthers[1].append(student)
		if(lastOthers<1):
			lastOthers = 1

	temp = []
	temp.append(student)
	temp.append(allottedPreference)
	OtherallottedPreference.append(temp)


def getHS(student):

	for entry in HSallottedPreference:
		if(entry[0]==student):
			return entry[1]
	aggHS[0].append(student)
	return "No preferences"

def getMA(student):

	for entry in MAallottedPreference:
		if(entry[0]==student):
			return entry[1]
	aggMA[0] += 1
	return "No preferences"

def getOthers(student):

	for entry in OtherallottedPreference:
		if(entry[0]==student):
			return entry[1]
	aggOthers[0].append(student)
	return "No preferences"




with open(saveDir+'allottedPreferences.csv', 'w') as csvfile:
    fieldnames = ['RollNumber', 'HSallottedPreference', 'MAallottedPreference', 'OtherallottedPreference']
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

    writer.writeheader()

    for student in students:
    	writer.writerow({'RollNumber':student, 'HSallottedPreference': getHS(student), 'MAallottedPreference': getMA(student), 'OtherallottedPreference': getOthers(student)})

lastToPrint = lastOthers
if(lastToPrint<lastHS):
	lastToPrint = lastHS
if(lastToPrint<lastMA):
	lastToPrint = lastMA


with open(saveDir+"preference-wise-stats.csv","w") as file_obj:
	file_obj.write("PreferenceNumber,HS,MA,Professional\n")
	for x in range(lastToPrint+1):
		if(x==0):
			file_obj.write("No Preferences,"+str(len(aggHS[0]))+","+str(aggMA[0])+","+str(len(aggOthers[0]))+"\n")
		elif(x==1):
			file_obj.write("No Course Allotted,"+str(len(aggHS[1]))+","+str(aggMA[1])+","+str(len(aggOthers[1]))+"\n")
		else:
			file_obj.write(str(x-1)+","+str(len(aggHS[x]))+","+str(aggMA[x])+","+str(len(aggOthers[x]))+"\n")

print("writing complete")

# pref = 9
# print aggOthers[pref+1]
# print len(aggOthers[pref+1])

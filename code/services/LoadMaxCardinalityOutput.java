package services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import models.Student;
import models.StudentPreference;
import models.Course;
import models.CoursePreference;

public class LoadMaxCardinalityOutput {

	public static void runAlgorithm(ArrayList<Student> studentList, String outputFile) {
		

		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		Student tempStudent;
		StudentPreference tempStudentPreference;
		
		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(outputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {			
				line=line.replaceAll("\\s+",""); //Remove all whitespace
				inputLine = line.split(splitBy);

				tempStudent = Student.getStudentByRollNo(inputLine[0], studentList);
				tempStudentPreference = StudentPreference.getStudentPreferenceBycourseNumber(tempStudent.studentPreferenceList, inputLine[1]);

				tempStudent.orderedListOfcoursesAllotted.add(tempStudentPreference);

				// Update the least preferred preference number for the course which is allotted.
				Course allottedCourse = tempStudentPreference.getCourseObj();
				CoursePreference cp = CoursePreference.getCoursePreferenceByRollNo(allottedCourse.coursePreferenceList,tempStudent.getRollNo());

				if(allottedCourse.leastPreferredAllottedStudent < cp.getPreferenceNo()){
					allottedCourse.leastPreferredAllottedStudent = cp.getPreferenceNo();
				}

			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	

	}

}
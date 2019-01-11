package services.dataInput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import models.Course;
import models.Student;
import models.StudentPreference;
import services.Constants;


public class PopulateStudentPreferenceList{
	/**
	 * This function populates the studentPreferenceList field of all students by reading the input preference list
	 * file (whose name is passed as an argument).
	 * 
	 * It also ensures that every student and course on the preference lists also exists in the student and course list respectively 
	 * @param courseSpecificInsideDeptInfo 
	 */
	public static String execute(ArrayList<Student> studentList,ArrayList<Course> courseList,String inputFile){
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		Student tempStudent;
		Course tempCourse;
		
		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {			
				line.replaceAll("\\s+",""); //Remove all whitespace
				inputLine = line.split(splitBy);
				
				//Get the various columns that are got after split the line
				String studentRollNumber = inputLine[0];
				String originalcourseNumber = inputLine[1];
					
				//Set preference number.
				int preferenceNumber = Integer.parseInt(inputLine[2]);
				
				//Get the student object corresponding to this preference
				tempStudent = Student.getStudentByRollNo(studentRollNumber,studentList);
				if (tempStudent == null){ //If the student does not exist in the student list - throw an error
		        	br.close();
		        	return "Student : " + studentRollNumber + " does not exist but has an entry in the student preference list";
		        }

				//Get the course object corresponding to this preference
				tempCourse = Course.getCourseBycourseNumber(originalcourseNumber, courseList);
				if (tempCourse==null){ //The course does not exist in the course list - error
					br.close();
					return "Course: " + originalcourseNumber + " does not exist, but the student " + tempStudent.getRollNo() + " has given a preference for it";
				}
				
				//Add to the list of courses
				tempStudent.studentPreferenceList.add(new StudentPreference(tempCourse,preferenceNumber));				
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Now that we have read the student preference list, loop through the set of students.
		for (Student st : studentList){
			//Sort the preference list based on the preference numbers provided
			Collections.sort(st.studentPreferenceList);
			//Now relabel the preference numbers : This may be necessary if the original preference numbers had gaps, and also because we could have introduced gaps when we created the preference object
			for (int i=0;i<st.studentPreferenceList.size();i++){
				st.studentPreferenceList.get(i).setPreferenceNumberToPositionInPreferenceList(st.studentPreferenceList);
			}
		}
		return null;
	}
}
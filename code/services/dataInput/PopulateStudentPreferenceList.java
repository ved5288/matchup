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
				line=line.replaceAll("\\s+",""); //Remove all whitespace
				inputLine = line.split(splitBy);

				int preferenceNumber = 1;
				

				//Get the student object corresponding to this preference
				tempStudent = Student.getStudentByRollNo(inputLine[0],studentList);
				if (tempStudent == null){ //If the student does not exist in the student list - throw an error
		        	br.close();
		        	return "Student : " + inputLine[0] + " does not exist but has an entry in the student preference list";
		        }
	            
		        for(int i=1;i<inputLine.length;i++){  //loop through the students and add them to the course's preference list
			        //Get the course object corresponding to this preference
					tempCourse = Course.getCourseBycourseNumber(inputLine[i], courseList);
					if (tempCourse==null){ //The course does not exist in the course list - error
						br.close();
						return "Course: " + inputLine[i] + " does not exist, but the student " + tempStudent.getRollNo() + " has given a preference for it";
					}

					//Add to the list of courses
					tempStudent.studentPreferenceList.add(new StudentPreference(tempCourse,preferenceNumber));
					preferenceNumber++;
		        }
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
}
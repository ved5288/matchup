package services.dataInput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import models.StudentClass;
import models.Student;
import models.StudentPreference;

public class PopulateStudentClassSpecifications{
	/**
	 * This method is used to load the course preference lists from an existing one
	 * @param studentList - List of students objects
	 * @param courseList - list of course objects
	 * @param inpFile - file where the course preference lists are located
	 * 
	 *It also ensures that every student and course on the preference lists also exists in the student and course list respectively 
	 */
	public static String execute(ArrayList<Student> studentList, String inpFile){
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		Student tempStudent;
		int maxCoursesInClass;
		StudentClass sc;
		//reading input line by line and adding a new student for every line.
		try{
			BufferedReader br = new BufferedReader(new FileReader(inpFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read line by line
			while ((line = br.readLine()) != null) {  
				line.replaceAll("\\s+",""); //Remove all whitespace
				inputLine = line.split(splitBy);

				tempStudent = Student.getStudentByRollNo(inputLine[0],studentList);
				if (tempStudent == null){ //The student does not exist in the student list : return error message
			        	br.close();
			        	return "Student : " + inputLine[0] + " does not exist but was in the student class specifications file";
			    }

			    maxCoursesInClass = Integer.parseInt(inputLine[1]);

			    sc = new StudentClass(maxCoursesInClass);

			    for(int i=2;i<inputLine.length;i++){  //loop through the students and add them to the course's preference list
			        
			        StudentPreference sp = StudentPreference.getStudentPreferenceBycourseNumber(tempStudent.studentPreferenceList,inputLine[i]);
			        
			        if(sp == null){ // The course doesn't exist in the preference list
			        	br.close();
			        	return "Course " + inputLine[i] + " does not exist in the student preference list of student " + inputLine[0] + " but has an entry in the student class specifications file";
			        }	

			        sc.coursesInThisClass.add(sp);
		        }

		        tempStudent.studentClasses.add(sc);

            }
			br.close();//closing file pointer
		//just some exception handling
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
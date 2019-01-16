package services.dataInput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import models.Course;
import models.CoursePreference;
import models.Student;

public class PopulateCoursePreferenceList{
	/**
	 * This method is used to load the course preference lists from an existing one
	 * @param studentList - List of students objects
	 * @param courseList - list of course objects
	 * @param inpFile - file where the course preference lists are located
	 * 
	 *It also ensures that every student and course on the preference lists also exists in the student and course list respectively 
	 */
	public static String execute(ArrayList<Student> studentList, ArrayList<Course> courseList,String inpFile){
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		Student tempStudent;
		Course tempCourse;

		//reading input line by line and adding a new student for every line.
		try{
			BufferedReader br = new BufferedReader(new FileReader(inpFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read line by line
			while ((line = br.readLine()) != null) {  
				line=line.replaceAll("\\s+",""); //Remove all whitespace
				inputLine = line.split(splitBy);
	            tempCourse = Course.getCourseBycourseNumber(inputLine[0],courseList);  //The first string has the course Id with which we can get the course object it corresponds to
	            if (tempCourse==null){ //The course does not exist in the course list : return error message
					br.close();
					return "Course : " + inputLine[0] + " does not exist, but it has an entry in the preference list";
				}
	            
		        for(int i=1;i<inputLine.length;i++){  //loop through the students and add them to the course's preference list
			        tempStudent = Student.getStudentByRollNo(inputLine[i],studentList);   
			        if (tempStudent == null){ //The student does not exist in the student list : return error message
			        	br.close();
			        	return "Student : " + inputLine[i] + " does not exist but was on the preference list of course " + inputLine[0];
			        }
			        tempCourse.coursePreferenceList.add(new CoursePreference(i,tempStudent));
		        }    
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
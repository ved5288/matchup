package services.dataInput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import models.StudentClass;
import models.Student;
import models.StudentPreference;

public class PopulateMasterClassSpecifications{
	/**
	 * This method is used to load the student class specifications
	 * @param studentList - List of students objects
	 * @param inpFile - file where the student class specifications are located
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

				// Add this class constraint to all the students
				for ( Student s : studentList ) {
					
					maxCoursesInClass = Integer.parseInt(inputLine[0]);
					sc = new StudentClass(maxCoursesInClass);

					for(int i=1;i<inputLine.length;i++){  //loop through the courses in the class and add them to the student class information.
			        
			        	StudentPreference sp = StudentPreference.getStudentPreferenceBycourseNumber(s.studentPreferenceList,inputLine[i]);
			        
				        if(sp != null){ // The course exists in the preference list
				        	sc.coursesInThisClass.add(sp);
				        }	
		        	}

		        	if(sc.coursesInThisClass.size()>maxCoursesInClass){ // Add the class to the student's info if there are at least maxCoursesInClass courses present in students preference lists.
		        		s.studentClasses.add(sc);
		        	}
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
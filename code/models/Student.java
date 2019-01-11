package models;

import java.util.ArrayList;

import services.Constants;
import services.ReasonsForNotAllottingPreferences;

/**
 * This class represents a physical student.
 */
public class Student{
	private String rollNo; //Read only.
	public int maxCreditsInSem; //The maximum credits that the student has stated he can take. It includes both elective and core credits. Read only.
	private int maxElectiveCreditsInSem; //The maximum  elective credits that the student can take. Will be calculated by subtracting the core course credits from 'maxCreditsInSem'. Read only.
	public int electiveCreditsLeft; //The elective credits left. This can change during the algorithm as this student gets allotted courses.
	public ArrayList<StudentPreference> studentPreferenceList; //Original preference list. Will not be modified once read. Is set to 'unmodifiable' in the DataInput
	public ArrayList<StudentPreference> coursesUnallotted; //Set of courses from the preference list which the student can still apply to. Can be modified during the algorithm
	public ArrayList<StudentPreference> orderedListOfcoursesAllotted; //List of courses from the preference list which the student has been allotted to. Can be modified during the algorithm. It is an ordered list in the sense that the order of the arraylist should maintain the order in which the allotments to these preferences was made. This order is important for the 'ReasonsForNotAllottingPreference' module, where we iterate through the ordered list of allotted courses to compute why a particular elective was not allotted.
	public ArrayList<StudentPreference> invalidPreferences;	//Set of courses from the preference list which are invalid (could be because of a slot clash with a core course, or that the course takes more credits that the student's maxElectiveCreditsInSem
	public ArrayList<StudentClass> studentClasses; //Set of additional constraints on the course allocation from the student end.
	public StudentPreference courseAllottedInCurrentIteration; //The course that the student is allotted in the current iteration of the iterative type of algorithms. Will be modified during the algorithm.
	
	//for statistics
	public double effectiveAverageRank; //Definition given in the documentation.
	public double creditSatisfactionRatio; //Definition given in the documentation.
	
	/* constructor :simply initializes the fields. */
	public Student(String inp_rollNo, int inp_maxCredits){
		rollNo = inp_rollNo;
		studentPreferenceList = new ArrayList<StudentPreference>();
		orderedListOfcoursesAllotted = new ArrayList<StudentPreference>();
		coursesUnallotted = new ArrayList<StudentPreference>();
		invalidPreferences = new ArrayList<StudentPreference>();
		studentClasses = new ArrayList<StudentClass>();
		maxCreditsInSem = inp_maxCredits;
		electiveCreditsLeft = inp_maxCredits;
		maxElectiveCreditsInSem = inp_maxCredits;
	}
	
	/**
	 * Getter methods for private fields. No setter methods since they are read only.
	 */
	public String getRollNo(){
		return rollNo;
	}
	public int getMaxElectiveCreditsInSem(){
		return maxElectiveCreditsInSem;
	}
	
	/**
	 * 
	 * @param inp_rollNo
	 * @param studentList
	 * @return = This function searches the studentList for a student with the given rollNo and returns the student object
	 */
	public static Student getStudentByRollNo(String inp_rollNo,ArrayList<Student> studentList){
		for (Student tempStudent : studentList){
			if (tempStudent == null){
				System.out.println("Student in student List is null, as seen in the getStudentByRollNo function. Exiting");
				System.exit(1);
			}
			else {
				if (tempStudent.rollNo.compareTo(inp_rollNo)==0){
					return tempStudent;
				}
					
			}
		}
		return null; //student not found
	}
	
	/**
	 * 
	 * @param inpCourse
	 * @return - returns true if this students has 'inpCourse' in his preference list. Otherwise returns false.
	 */
	public boolean hasCourseInPreferences(Course inpCourse){
		for (int i=0;i<studentPreferenceList.size();i++){
			if (studentPreferenceList.get(i).getCourseObj().getcourseNumber().equalsIgnoreCase(inpCourse.getcourseNumber())){
				return true;
			}
		}
		return false;
	}

}
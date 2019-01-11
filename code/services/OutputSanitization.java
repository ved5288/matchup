package services;

import java.util.ArrayList;

import models.Course;
import models.Student;
import models.StudentClass;
import models.StudentPreference;

public class OutputSanitization {

	/*
	 * The following 5 checks are performed to check if there are no errors in the output of the algorithm
	 */

	public static String sanitize(ArrayList<Student> studentList, ArrayList<Course> courseList) {
		String error1 = checkForDuplicateAllotmentForStudent(studentList);
		if (error1!=null){
			return error1;
		}
		String error2 = checkIfCreditLimitOfStudentSatisfied(studentList);
		if (error2!=null){
			return error2;
		}
		String error3 = checkIfCourseCapacitiesSatisfied(studentList,courseList);
		if (error3!=null){
			return error3;
		}
		String error4 = checkIfAllottedCoursesPartOfPreferenceList(studentList);		
		if (error4!=null){
			return error4;
		}
		String error5 = checkIfClassConstraintsSatisfied(studentList);
		if (error5!=null){
			return error5;
		}
		//Else if there have been no errors
		return null;
	}

	private static String checkIfClassConstraintsSatisfied(ArrayList<Student> studentList) {
		for (Student s : studentList){
			for (StudentClass sc : s.studentClasses) {
				int coursesAllottedFromThisClass = 0;
				
				for(StudentPreference sp : s.orderedListOfcoursesAllotted){
					if(sc.coursesInThisClass.contains(sp)){
						coursesAllottedFromThisClass++;
					}
				}

				if(coursesAllottedFromThisClass>sc.getMaxCoursesToBeAllotted()){
					return ("Class constraints not met for student : " + s.getRollNo() );
				}
			}
		}
		return null; //No class constraint issue
	}

	private static String checkIfAllottedCoursesPartOfPreferenceList(ArrayList<Student> studentList) {
		for (Student s : studentList){
			for (StudentPreference sp : s.orderedListOfcoursesAllotted){
				if (!s.studentPreferenceList.contains(sp)){ //The allotted course is not a part of the student's preference list
					return ("For student : " + s.getRollNo() + " allotted course " + sp.getCourseObj().getcourseNumber() + " is not a part of his preference list");
				}
			}
		}
		return null; //Test passed. No issues.
	}

	private static String checkIfCourseCapacitiesSatisfied(ArrayList<Student> studentList, ArrayList<Course> courseList) {
		for (Course c : courseList){
			int allottedStudentsForCourseC = 0;
			for (Student s : studentList){
				for (StudentPreference sp : s.orderedListOfcoursesAllotted){
					if (sp.getCourseObj()==c){
						allottedStudentsForCourseC += 1;
					}
				}
			}
			if (allottedStudentsForCourseC>c.getCapacity()){
				return ("Course number " + c.getcourseNumber() + " has capacity of " + c.getCapacity() + " but was allotted " + allottedStudentsForCourseC + " students");
			}
		}
		return null;
	}

	private static String checkIfCreditLimitOfStudentSatisfied(ArrayList<Student> studentList) {
		for (Student s : studentList){
			int creditsAllottedForStudent = 0;
			for (StudentPreference sp : s.orderedListOfcoursesAllotted){
				creditsAllottedForStudent += sp.getCourseObj().getCredits();
			}
			if (creditsAllottedForStudent>s.maxCreditsInSem){
				return ("For student : " + s.getRollNo() + " allotted credits is " + creditsAllottedForStudent + " but max credits given in input is " + s.maxCreditsInSem); 
			}
		}
		return null;
	}

	private static String checkForDuplicateAllotmentForStudent(ArrayList<Student> studentList) {
		for (Student s : studentList){
			for (StudentPreference sp1 : s.orderedListOfcoursesAllotted){
				//For every course in the set of allotted courses, check if it occurs more than once in the allotted courses list
				int numberOfOccurencesOfsp1 = 0;
				for (StudentPreference sp2 : s.orderedListOfcoursesAllotted){
					//If the 'courseNumber' fields match, it is a duplicate
					if (sp1.getCourseObj().getcourseNumber().equalsIgnoreCase(sp2.getCourseObj().getcourseNumber())){
						numberOfOccurencesOfsp1 += 1;
					}
				}
				if (numberOfOccurencesOfsp1>1){
					return ("For student : " + s.getRollNo() + " the allotted course number  " + sp1.getCourseObj().getcourseNumber() + " is duplicate "); 
				}
			}
		}
		return null;
	}
}

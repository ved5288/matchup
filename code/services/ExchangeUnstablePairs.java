package services;

import java.util.ArrayList;

import models.Course;
import models.Student;
import models.StudentPreference;
import models.StudentClass;
/**
 * This function is mainly used compute the exchange unstable pairs.
 * A pair of students is exchange unstable if they each have 1 allotted course that
 * they would exchange with the other student, such that both students
 * are better off in the allotment (lexicographically better off - read lexicographical order comparison online)
 * @author vedant - modified the code written by akshay
 *
 */
public class ExchangeUnstablePairs {
	public static String computeExchangeUnstablePairs(ArrayList<Student> studentList) {
		String exchangeUnstablePairs = "";
		//Loop through every pair of course allotments
		for (int i=0;i<studentList.size();i++){
			Student s1 = studentList.get(i);
			for (int j=i+1;j<studentList.size();j++){
				Student s2 = studentList.get(j);
				for (StudentPreference sp1 : s1.orderedListOfcoursesAllotted){
					for (StudentPreference sp2 : s2.orderedListOfcoursesAllotted){
						
						//Firstly ensure that the 2 courses for exchange occour on the 2 preference lists
						if (StudentPreference.getStudentPreferenceBycourseNumber(s1.studentPreferenceList,sp2.getCourseObj().getcourseNumber())==null 
								|| StudentPreference.getStudentPreferenceBycourseNumber(s2.studentPreferenceList,sp1.getCourseObj().getcourseNumber())==null){
							continue; //If the courses to be exchanged don't occour on the other student's preference list, exchange instability not possible here
						}
						//Check for exchange instability only if the exchange benefits both students
						if (sp1.getPreferenceNo() < StudentPreference.getStudentPreferenceBycourseNumber(s1.studentPreferenceList,sp2.getCourseObj().getcourseNumber()).getPreferenceNo()
							|| sp2.getPreferenceNo() < StudentPreference.getStudentPreferenceBycourseNumber(s2.studentPreferenceList,sp1.getCourseObj().getcourseNumber()).getPreferenceNo()){
							continue; //If the exchange does not benefit both students, exchange instability not possible here
						}
						
						//The 2 preferences should not refer to the same course
						if (sp1.getCourseObj()==sp2.getCourseObj()){
							continue; //If true, exchange instability not possible here
						}
							
						//sp2 should not violate class constraints with the allotted elective courses (apart from sp1) of s1
						if (checkElecitveCourseConstrained(s1,sp2,sp1)){
							continue; //If there is a violation, exchange instability not possible here
						}
							
						//sp1 should not violate class constraints with the allotted elective courses (apart from sp2) of s2
						if (checkElecitveCourseConstrained(s2,sp1,sp2)){
							continue; //If there is a violation, exchange instability not possible here
						}
							
						//sp2.getCourseObj() should fit into the remaining credits for s1 (after removing sp1, because we are trying to exchange)
						if (!checkIfCourseFitsInCredits(s1,sp2.getCourseObj(),sp1.getCourseObj())){
							continue; //If the course does not fit in the remaining credits, exchange instability not possible here
						}
							
						//sp1.getCourseObj() should fit into the remaining credits for s2 (after removing sp2, because we are trying to exchange)
						if (!checkIfCourseFitsInCredits(s2,sp1.getCourseObj(),sp2.getCourseObj())){
							continue; //If the course does not fit in the remaining credits, exchange instability not possible here
						}
							
						//If we reached this far, the 2 courses can be easily exchanged in order to get a
						//better allotment for both students. Record it
						exchangeUnstablePairs += s1.getRollNo() + "," + sp1.getCourseObj().getcourseNumber() + "," + s2.getRollNo() + "," + sp2.getCourseObj().getcourseNumber() + "\n";
					}
				}
			}
		}
		return exchangeUnstablePairs;
	}

	
	/**
	 * @param s
	 * @param exchangeCourse : The course which has to be exchanged in
	 * @param exceptionCourse : The course which has to be exchanged out
	 * @return : Checks scope of 'exchangeCourse' and the allotted elective courses of student 's' (except for the 'exceptionCourse')
	 */
	private static boolean checkElecitveCourseConstrained(Student s, StudentPreference exchangeCourse, StudentPreference exceptionCourse) {

		for (StudentClass sc : s.studentClasses) {
			if(sc.coursesInThisClass.contains(exchangeCourse)){
				int numOfCoursesAllottedFromThisClass = 0;
				for (StudentPreference allotted : s.orderedListOfcoursesAllotted) {
					if(allotted != exceptionCourse && sc.coursesInThisClass.contains(allotted)){
						numOfCoursesAllottedFromThisClass++;
					}
				}
				if(sc.getMaxCoursesToBeAllotted()==numOfCoursesAllottedFromThisClass){ // The class constraint is met and that there is no possibility of exchanging the course
					return true;
				}
			}
		}

		//If we reached here, no clash with allotted electives
		return false;
	}
	
	/**
	 * 
	 * @param s
	 * @param courseObj
	 * @param exceptionCourse
	 * @return Check if courseObj fits in with all the core+allottedElective courses of s, (except for the 
	 * exceptionCourse, because that is the course we are going to exchange out.)
	 */
	private static boolean checkIfCourseFitsInCredits(Student s, Course courseObj, Course exceptionCourse) {
		int totalCredits = 0;
		
		//Count elective credits (except for the exception course)
		for (StudentPreference sp : s.orderedListOfcoursesAllotted){
			if (sp.getCourseObj()!=exceptionCourse){
				totalCredits += sp.getCourseObj().getCredits();
			}
		}
		
		//Check if the course fits in the remaining credits
		if (courseObj.getCredits() <= s.maxCreditsInSem - totalCredits){
			return true;
		}
		else{
			return false;
		}		
	}
}

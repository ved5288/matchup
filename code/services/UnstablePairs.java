package services;

import java.util.ArrayList;

import models.Course;
import models.Student;
import models.StudentPreference;
import models.CoursePreference;
import models.StudentClass;
/**
 * This function is mainly used compute the unstable pairs.
 * A pair (s,c) is defined as unstable 
 * a) if they prefer each other over their least preferred allotted partners and 
 * b) 'c' is feasible with the set of courses which are allotted to 's' and ranked above 'c'
 * The code assumes that the matching is maximal, that is a new course cannot be allotted at this juncture without replacing other courses
 * @author Vedant
 *
 */
public class UnstablePairs {
	public static String computeUnstablePairs(ArrayList<Student> studentList) {
		String unstablePairs = "";
		//Loop through every student preference
		for (Student s : studentList){

			for (int i=0;i<s.studentPreferenceList.size();i++){
				StudentPreference sp = s.studentPreferenceList.get(i);
		

				//Ignore if the preference is allotted
				if( s.orderedListOfcoursesAllotted.contains(sp)){
					continue;
				}

				//Ignore if the course does not prefer this student over its least preferred partner
				Course c = sp.getCourseObj();
				CoursePreference cp = CoursePreference.getCoursePreferenceByRollNo(c.coursePreferenceList, s.getRollNo());

				if(cp.getPreferenceNo() > c.leastPreferredAllottedStudent){
					continue;
				}

				// If we reach here, course prefers the student over its least preferred matched student.

				for ( int j=i+i; j<s.studentPreferenceList.size(); j++){
					StudentPreference replaceSP = s.studentPreferenceList.get(j);

						// Ignore if a lower unallotted preference is seen
						if(!s.orderedListOfcoursesAllotted.contains(replaceSP)){
							continue;
						}

						//sp should not violate class constraints with the allotted elective courses (apart from replaceSP) of s
						if (checkElecitveCourseConstrained(s,sp,replaceSP)){
							continue; //If there is a violation, unstability not possible here
						}
							
						//sp.getCourseObj() should fit into the remaining credits for s (after removing replaceSP, because we are trying to exchange)
						if (!checkIfCourseFitsInCredits(s,sp.getCourseObj(),replaceSP.getCourseObj())){
							continue; //If the course does not fit in the remaining credits, exchange instability not possible here
						}

						//If we reached this far, we found an unstable pair. Record it
						unstablePairs += s.getRollNo() + "," + c.getcourseNumber() + "," + c.getStudentOfPreferenceNumber(c.leastPreferredAllottedStudent).getRollNo() +"\n";
				}

			}
		}

		return unstablePairs;
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

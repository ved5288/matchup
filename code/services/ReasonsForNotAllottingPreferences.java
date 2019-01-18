package services;

import java.util.ArrayList;
import java.util.Iterator;

import models.Course;
import models.Student;
import models.StudentPreference;
import models.StudentClass;

public class ReasonsForNotAllottingPreferences {

	public static void computeReasonsonsForNotAllottingPreferences(
			ArrayList<Student> originalStudentList) {
		
		for (Student s : originalStudentList){
			//For each invalid preference compute the reason why it is not allotted
			for (StudentPreference sp : s.invalidPreferences){
				sp.reasonForNotAllottingThisPreference = reasonForNotAllottingInvalidPreference(sp,s,originalStudentList);
				sp.detailedReasonForNotAllottingThisPreference = detailedReasonForNotAllottingInvalidPreference(sp,s,originalStudentList);
			}
			
			//For each valid preference compute the reason why it is not allotted, in case it was not allotted
			for (StudentPreference sp : s.studentPreferenceList){
				if (!s.orderedListOfcoursesAllotted.contains(sp)){
					sp.detailedReasonForNotAllottingThisPreference = reasonForNotAllottingValidPreference(sp,s,originalStudentList,true);
					sp.reasonForNotAllottingThisPreference = reasonForNotAllottingValidPreference(sp,s,originalStudentList,false);
				}	
			}
			
		}
		
		
	}

	//The function tries out all the possible reasons why this preference is declared invalid
	private static String detailedReasonForNotAllottingInvalidPreference(StudentPreference invalidElective, Student s, ArrayList<Student> originalStudentList) {
		
		//Check if the credits for the preference is greater than 'maxElectiveCreditsInSem' for student
		if (invalidElective.getCourseObj().getCredits() > s.getMaxElectiveCreditsInSem()){
			String creditsViolationReason = "For this student max credits in semester is " + s.maxCreditsInSem ;
			creditsViolationReason = creditsViolationReason + " and " + invalidElective.getCourseObj().getcourseNumber() + " takes " + invalidElective.getCourseObj().getCredits() + " credits which is greater than the " + s.getMaxElectiveCreditsInSem() + " remaining elective credits.";
			return creditsViolationReason;
		}
		
		//If we return null - there is probably a bug in the code. We should have caught the reason by this point.
		return null;
	}

	//The function tries out all the possible reasons why this preference is declared invalid
	private static String reasonForNotAllottingInvalidPreference(StudentPreference invalidElective, Student s, ArrayList<Student> originalStudentList) {
		
		//Check if the credits for the preference is greater than 'maxElectiveCreditsInSem' for student
		if (invalidElective.getCourseObj().getCredits() > s.getMaxElectiveCreditsInSem()){
			String creditsViolationReason = "Insufficient credit balance.";
			return creditsViolationReason;
		}
		
		//If we return null - there is probably a bug in the code. We should have caught the reason by this point.
		return null;
	}


	//The function tries out all the possible reasons why this preference was not allotted 
	//(It is algorithm agnostic - i.e. it does not use the information of which algorithm was used)
	private static String reasonForNotAllottingValidPreference(StudentPreference validPreference, Student s, ArrayList<Student> originalStudentList, boolean detailed) {
		/* The seven possible reasons for not getting allotted a valid preference is
		 * 2. Class constraint satisfied by courses which were ranked higher by student
		 * 3. The set of core courses and elective courses which were ranked higher by this student fill up the credit requirement,
		 *    and this course does not fit in the remaining credits
		 * 4. Course is full
		 * 6. Course with same colour code already allotted, which was ranked lower by student (will not occour for IterativeHR, firstPreferenceAllotment. Can occour for slotBasedHR)
		 * 7. The set of core courses and elective courses which were ranked lower by student (will not occour for IterativeHR, firstPreferenceAllotment. Can occour for slotBasedHR) fill up the credit requirement,
		 *    and this course does not fit in the remaining credits
		 *    
		 * Note that the order of this check must be maintained because of these points.
		 * a) Reason 1 to 3 can be permuted among themselves and it will be okay.
		 * b) Reason 4 must be checked after Reason 1,2,3 because of the following. If a 
		 *    student is not allotted course c1 because of one of the reasons (1,2,3), but the course
		 *    ends up full and Reason 4 is reported because it was checked first, it could be a problem. 
		 *    If the student knows that c1 ranks him/her well according to its ranking criteria (say CGPA), and
		 *    other students in his class with a lower CGPA got the course, then reading the 
		 *    Reason 4 will make him think that the allotment is unfair. Hence in the presence
		 *    of Reason 1,2,3 being true for not allotting, Reason 4 should not be stated as 
		 *    a reason.
		 * c) Reason 5,6,7 can permuted among themselves.
		 * d) Reason 5,6,7 must come after Reason 1,2,3,4. If a student is told Reason 5,6,7 
		 * 	  for not getting allotted course c1, in the presence of Reason 1,2,3,4 being true,
		 *    then the student may find it unfair. For example if course c1 was not allotted
		 *    because of Reason 4, and you state reason 5 (even reason 5 is true),
		 *    the student will question why a lower ranked course is blocking his higher
		 *    rank course. But the truth is that the course became full. Another example is 
		 *    if both Reason 2 and 5 are true, the student will find Reason 2 to be a better
		 *    explanation than Reason 5. And in this case it was probably Reason 2 that 
		 *    was the actual cause in the algorithm
		 * e) Reason 5,6,7 seem like they will not occur, but they can in the case of slot 
		 *    based HR, where we may first pick a slot which has low ranked courses 
		 *    for a student, and his low rank courses may block (by slot,colour,credits) a
		 *    higher ranked course. This problem will not occour in IterativeHR and 
		 *    FirstPreferenceAllotment
		 * f) Reason 0 must come first for obvious reasons.  
		 *    
		 */
		
		String reason = null;
				
		//2. Check for Course with same colour code already allotted, which was ranked higher by student
		reason = checkClassConstraint(validPreference,s);
		if (reason!=null){
			return reason;
		}
		
		//3. Check if the set of core courses and elective courses which were ranked higher by this 
		//student fill up the credit requirement, and this course does not fit in the remaining credits
		if(detailed){
			reason = checkIfCreditsFinishedWithHigherRankingCoursesInDetail(validPreference,s);	
		} else {
			reason = checkIfCreditsFinishedWithHigherRankingCourses(validPreference,s);
		}
		if (reason!=null){
			return reason;
		}
							
		//4. Check if Course is full
		reason = checkIfCourseIsFull(validPreference,s,originalStudentList);
		if (reason!=null){
			return reason;
		}
				
		//7. Check if the set of core courses and elective courses which were ranked higher as well as lower by this 
		//student fill up the credit requirement, and this course does not fit in the remaining credits
		reason = checkIfCreditsFinishedWithAllCourses(validPreference,s);
		if (reason!=null){
			return reason;
		}
		
		//We have a problem if we have not found the reason for not allotting.
		System.out.println("ERROR : In module 'ReasonsForNotAllottingPreferences' could not catch reason for not allotting course");
		System.exit(0);
		return null;
	}
	
	private static String checkIfCreditsFinishedWithAllCourses(StudentPreference validPreference, Student s) {
		int totalElectiveCreditsAllotted = 0;
		//First count the credits of all allottedCredits
		for (StudentPreference allottedElective : s.orderedListOfcoursesAllotted){
			totalElectiveCreditsAllotted += allottedElective.getCourseObj().getCredits();
		}
		
		//Next check if the 'totalElectiveCreditsAllotted' along with the new course exceeds the maxElectiveCreditsInSem
		int remainingCredits = s.getMaxElectiveCreditsInSem() - totalElectiveCreditsAllotted;
		if (validPreference.getCourseObj().getCredits() > remainingCredits){
			//List the Elective credits for electives at a higher preference
			String creditsViolationReason = "For this student max credits in semester is " + s.maxCreditsInSem + " and the following electives got allotted : ";
			
			for (StudentPreference allottedElective : s.orderedListOfcoursesAllotted){
				creditsViolationReason += allottedElective.getCourseObj().getcourseNumber() + "(" + allottedElective.getCourseObj().getCredits() + " credits)+";
			}
			
			//Conclude the reason with the student's credits
			creditsViolationReason += " and " + validPreference.getCourseObj().getcourseNumber() + " takes " + validPreference.getCourseObj().getCredits() + " credits which is greater than the remaining " + remainingCredits + " elective credits.";
			return creditsViolationReason;
		}
		
		//If this is not the reason for the allotment not happening, return null		
		return null;
	}

	private static String checkIfCourseIsFull(StudentPreference validPreference, Student student, ArrayList<Student> originalStudentList) {
		int numberOfPeopleAllottedToThisCourse = 0;
		//Loop through the student list to find out which students were allotted to this course
		for (Student s : originalStudentList){
			for (StudentPreference sp : s.orderedListOfcoursesAllotted){
				if (sp.getCourseObj() == validPreference.getCourseObj()){
					numberOfPeopleAllottedToThisCourse += 1;
				}
			}
		}
		
		//Now check if the number of people allotted is equal to the capacity
		if (validPreference.getCourseObj().getCapacity() == numberOfPeopleAllottedToThisCourse){
			return "Course capacity is full.";
		}
		
		//If this is not the reason for the allotment not happening, return null		
		return null;
	}

	
	private static String checkIfCreditsFinishedWithHigherRankingCoursesInDetail(StudentPreference validPreference, Student s) {
		int electiveCreditsAllottedForHigherPreferences = 0;
		//First count the credits of the courses ranked above
		for (StudentPreference allottedElective : s.orderedListOfcoursesAllotted){
			if (allottedElective.getPreferenceNo()<validPreference.getPreferenceNo()){
				electiveCreditsAllottedForHigherPreferences += allottedElective.getCourseObj().getCredits();
			}
		}
		
		//Next check if the 'totalElectiveCreditsAllotted' along with the new course exceeds the maxElectiveCreditsInSem
		int remainingCredits = s.getMaxElectiveCreditsInSem() - electiveCreditsAllottedForHigherPreferences;
		if (validPreference.getCourseObj().getCredits() > remainingCredits){
			//List the Elective credits for electives at a higher preference
			String creditsViolationReason = "For this student max credits in semester is " + s.maxCreditsInSem + " and the following electives which have a higher preference got allotted : ";
			
			for (StudentPreference allottedElective : s.orderedListOfcoursesAllotted){
				if (allottedElective.getPreferenceNo()<validPreference.getPreferenceNo()){
					creditsViolationReason += allottedElective.getCourseObj().getcourseNumber() + "(" + allottedElective.getCourseObj().getCredits() + " credits)+";
				}
			}
			
			//Conclude the reason with the student's credits
			creditsViolationReason += " and " + validPreference.getCourseObj().getcourseNumber() + " takes " + validPreference.getCourseObj().getCredits() + " credits which is greater than the remaining " + remainingCredits + " elective credits.";
			return creditsViolationReason;
		}
		
		//If this is not the reason for the allotment not happening, return null		
		return null;
	}

	private static String checkIfCreditsFinishedWithHigherRankingCourses(StudentPreference validPreference, Student s) {
		int electiveCreditsAllottedForHigherPreferences = 0;
		//First count the credits of the courses ranked above
		for (StudentPreference allottedElective : s.orderedListOfcoursesAllotted){
			if (allottedElective.getPreferenceNo()<validPreference.getPreferenceNo()){
				electiveCreditsAllottedForHigherPreferences += allottedElective.getCourseObj().getCredits();
			}
		}
		
		//Next check if the 'totalElectiveCreditsAllotted' along with the new course exceeds the maxElectiveCreditsInSem
		int remainingCredits = s.getMaxElectiveCreditsInSem() - electiveCreditsAllottedForHigherPreferences;
		if (validPreference.getCourseObj().getCredits() > remainingCredits){
			//List the core credits for the student
			String creditsViolationReason = "Insufficient credit balance.";
			return creditsViolationReason;
		}
		
		//If this is not the reason for the allotment not happening, return null		
		return null;
	}


	
	private static String checkClassConstraint(StudentPreference validPreference, Student s) {

		for (StudentClass sc : s.studentClasses ) {
			
			if(sc.coursesInThisClass.contains(validPreference)){

				ArrayList<StudentPreference> coursesAllottedInThisClass = new ArrayList<StudentPreference>();

				for (StudentPreference allottedElective : s.orderedListOfcoursesAllotted) {
					if(sc.coursesInThisClass.contains(allottedElective) && validPreference.compareTo(allottedElective) == 1){
						coursesAllottedInThisClass.add(allottedElective);
					}
				}

				if(sc.getMaxCoursesToBeAllotted()==coursesAllottedInThisClass.size()){ // Class constraint is satisfied
					String reason = "";
					reason += "Class constraint satisfied by already allotted elective courses : ";
					for (StudentPreference elective : coursesAllottedInThisClass) {
						reason += elective.getCourseObj().getcourseNumber() + "," ;
					}
					return reason;
				}
			}
		}

		//If this is not the reason for the allotment not happening, return null
		return null;
	}

}

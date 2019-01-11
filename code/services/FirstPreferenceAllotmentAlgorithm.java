package services;

import java.util.ArrayList;

import models.Course;
import models.CoursePreference;
import models.Student;
import models.StudentPreference;


public class FirstPreferenceAllotmentAlgorithm {

	public static void runAlgorithm(ArrayList<Student> studentList,ArrayList<Course> courseList) {
		//some declarations
		Student tempStudent;
		
		/*Initialization*/
		//Set every student's unallotted courses as his entire preferenceList initially, and allotted courses as empty list
		for (int i=0;i<studentList.size();i++){
			tempStudent = studentList.get(i);
			tempStudent.coursesUnallotted = CommonAlgorithmUtilities.copyArrayList(tempStudent.studentPreferenceList);
			tempStudent.orderedListOfcoursesAllotted = new ArrayList<StudentPreference>();
		}
					
		//Compute the set of students who have capacity to take atleast 1 course on their preference list
		ArrayList<Student> studentsWithCapacityLeft = CommonAlgorithmUtilities.copyArrayList(studentList);
		
		/*Actual Algorithm Loop*/
		//The iterations of this loop correspond to the iterations in the name 'IterativeHR'
		while (studentsWithCapacityLeft.size()>0){
			/* Initialization for this iteration */
			//Set every course's allotted list for this iteration to be zero
			for (Course c : courseList){
				c.currentIterationStudentAllottedList = new ArrayList<CoursePreference>();
			}
			
			//Set every student's allotted course for this iteration to be nothing
			for (Student s : studentList){
				s.courseAllottedInCurrentIteration = null;
			}
			
			//Remove students with an empty preference list - necessary here. Else the 'allotFirstPreferences()' will crash if it encounters an empty preference list
			CommonAlgorithmUtilities.removeStudentsWithNoCourseLeftToApplyTo(studentsWithCapacityLeft);
			
			//Set the list of unallotted students for this iteration to be all the students who have capacity left
			ArrayList<Student> unallottedStudentsInIteration = CommonAlgorithmUtilities.copyArrayList(studentsWithCapacityLeft);
			
			//Allot each student his first preference (This is the function used in place of HR)
			allotFirstPreferences(unallottedStudentsInIteration);
			
			//Update some values that change after the HR iteration
			CommonAlgorithmUtilities.recalculateStudentsLeftoverCredits(studentsWithCapacityLeft);
			CommonAlgorithmUtilities.freezeAllotmentOfCurrentIteration(studentsWithCapacityLeft);
			CommonAlgorithmUtilities.recalculateStudentClassScopes(studentsWithCapacityLeft);
			
			//Remove Preferences that become invalid because of constraint or credit limit exceeding
			CommonAlgorithmUtilities.removeStudentPreferencesAccordingToClassConstraints(studentsWithCapacityLeft);
			CommonAlgorithmUtilities.removeStudentPreferencesWithCreditRequirementExceedingCreditLimit(studentsWithCapacityLeft);
			
			//Remove students with no capacity left
			CommonAlgorithmUtilities.removeStudentsWithNoCourseLeftToApplyTo(studentsWithCapacityLeft);	 
		}	//Note that at the end of each iteration we have a valid allotment
	}

	//This is the function used in place of HR
	//We allot each student to his first preference course, and each oversubscribed course (excess of 'k') removes its worst 'k' students
	private static void allotFirstPreferences(ArrayList<Student> unallottedStudentsInIteration) {
		//some declarations
		Student tempStudent;
		Course tempCourse = null;
		StudentPreference tempStudentPreference;
		CoursePreference tempCoursePreference;
		
		/*Run the classical HR algorithm within this loop with one small change : If a student is rejected from his top choice course, don't give him a chance to apply to another course, until the next iteration*/
		while (!unallottedStudentsInIteration.isEmpty()){

			//Pick the first student from the list of unallotted students for this iteration
			tempStudent = unallottedStudentsInIteration.remove(0);
			//get the 1st course on his preference list (i.e. unallotted courses list)
			tempStudentPreference = tempStudent.coursesUnallotted.remove(0);
			
			//add the course as the student's allotted course
			tempStudent.courseAllottedInCurrentIteration = tempStudentPreference;
			
			//Add the student to the course's allotted list
			tempCourse = tempStudentPreference.getCourseObj();
			tempCoursePreference = CoursePreference.getCoursePreferenceByRollNo(tempCourse.coursePreferenceList,tempStudent.getRollNo());
			if (tempCoursePreference==null){
				System.out.println("ERROR during FirstPreference Algorithm. getCoursePreferenceByRollNo() function returned null");
				System.exit(0);
			}
			tempCourse.currentIterationStudentAllottedList.add(tempCoursePreference);
			
			//At this point
			//if the course is not full - no problem
			if (tempCourse.capacityStillFree != 0){
				tempCourse.capacityStillFree = tempCourse.capacityStillFree-1;  //just decrement the capacity still free counter by 1
			}
			//else we need to evict the worst student
			else{
				int maxPreferenceNo = 0;  // a course_preference with the maxPreferenceNo is the worst course_preference
				CoursePreference worstCoursePreference = null;
				
				//find the worst course preference
				for (CoursePreference cp : tempCourse.currentIterationStudentAllottedList){
					if (cp.getPreferenceNo() > maxPreferenceNo){
						maxPreferenceNo = cp.getPreferenceNo();
						worstCoursePreference = cp;
					}
				}
				
				//increment the course's noOfRejections by 1, since it rejected a person
				tempCourse.noOfRejections +=1;
					
				//evict (delete) that course preference object
				tempCourse.currentIterationStudentAllottedList.remove(worstCoursePreference);
				worstCoursePreference.getStudentObj().courseAllottedInCurrentIteration = null;
				
				/*Unlike the HR algorithm, this step is skipped. If we don't skip it, it becomes HR
				//If his courseUnallotedList is non empty, add him back to 'unallottedStudents'
				if (!worstCoursePreference.studentObj.coursesUnallotted.isEmpty()){
					unallottedStudentsInIteration.add(worstCoursePreference.studentObj);
				}
				*/
			}
		}
	}
}
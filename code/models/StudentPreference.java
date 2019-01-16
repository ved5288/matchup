package models;

import java.util.ArrayList;

/**
 * Objects of this class represent 1 entry on 1 student's preference list.
 */
public class StudentPreference implements Comparable<StudentPreference>{
	private Course courseObj; //Pointer to the course object which this student preference points to. Read only.
	private int preferenceNo; //The preference number of this course on the student's preference list. It will only be an integer and is final. Read only.
	public String reasonForNotAllottingThisPreference; //This field will be used to store the reason why this preference was not allotted in a concise manner
	public String detailedReasonForNotAllottingThisPreference; //This field will be used to store the reason why this preference was not allotted in a detailed manner
	
	/* constructor simply initializes the fields. */
	public StudentPreference(Course inp_courseObj, int inp_preferenceNo){
		courseObj = inp_courseObj;
		preferenceNo = inp_preferenceNo;
		reasonForNotAllottingThisPreference = null;
		detailedReasonForNotAllottingThisPreference = null;
	}

	/**
	 * Getter methods for the fields of this class. (No setters provided since they shouldn't be modified)
	 */
	public Course getCourseObj(){
		return courseObj;
	}
	public int getPreferenceNo(){
		return preferenceNo;
	}
	
	//Given a student preference list, it returns a student preference based on the courseNumber
	public static StudentPreference getStudentPreferenceBycourseNumber(ArrayList<StudentPreference> preferenceList,String inpcourseNumber){
		for (int i=0;i<preferenceList.size();i++){
			if (preferenceList.get(i).courseObj.getcourseNumber().equals(inpcourseNumber)){
				return preferenceList.get(i);
			}
		}
		return null;
	}

	/*This function is necessary since this class implements the Comparable interface.
	 * This allows us to use the Collections.sort() method to sort the course preferences
	 */
	@Override
	public int compareTo(StudentPreference sp) {
		if (preferenceNo<sp.preferenceNo){
			return -1;
		}
		else if (preferenceNo>sp.preferenceNo){
			return 1;
		}
		else{
			System.out.println("ERROR : 2 student Preferences with the same preference number in the StudentPreference.compareTo function :" + sp.courseObj.getcourseNumber() + "," + courseObj.getcourseNumber());
			System.exit(0);
			return 0;
		}
	}

	
}

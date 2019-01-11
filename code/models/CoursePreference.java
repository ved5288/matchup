package models;

import java.util.ArrayList;

/**
 * Objects of this class represent 1 entry in 1 course's preference list.
 */
public class CoursePreference{
	private Student studentObj;
	private int preferenceNo;
	
	/* Constructor */
	public CoursePreference(int inp_preferenceNo,Student inp_studentObj){
		studentObj = inp_studentObj;
		preferenceNo = inp_preferenceNo;
	}
	
	/*
	 * Getter methods for private fields. There are no corresponding setter methods since it is intended to be read only
	 */
	public Student getStudentObj(){
		return studentObj;
	}
	public int getPreferenceNo(){
		return preferenceNo;
	}
	/**
	 * 
	 * @param preferenceList
	 * @param inpRollNo
	 * @return - the course_preference object in the 'preferenceList' that has rollNo as 'inpRollNo'
	 */
	public static CoursePreference getCoursePreferenceByRollNo (ArrayList<CoursePreference> preferenceList,String inpRollNo){
		
		for (int i=0;i<preferenceList.size();i++){
			if (preferenceList.get(i).studentObj.getRollNo() == inpRollNo){
				return preferenceList.get(i);
			}
		}
		return null;
	}
}
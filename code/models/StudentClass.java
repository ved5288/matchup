package models;

import java.util.ArrayList;
import java.util.HashMap;

import services.Constants;


/**
 * This class represents a student defined class with additional constraints on the preferences
 */
public class StudentClass{ //Note that the terms 'course number' and 'courseNumber' have been used interchangeably. They both refer to the course number like CS2200.
	
	private int maxCoursesToBeAllotted;
	public ArrayList<StudentPreference> coursesInThisClass;
	public int scopeOfAllotment;

	/* constructor :simply initializes the fields. */
	public StudentClass(int inp_maxCourses){
		maxCoursesToBeAllotted = inp_maxCourses;
		coursesInThisClass = new ArrayList<StudentPreference>();
		scopeOfAllotment = inp_maxCourses;
	}


	public int getMaxCoursesToBeAllotted(){
		return maxCoursesToBeAllotted;
	}

}
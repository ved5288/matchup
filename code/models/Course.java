package models;

import java.util.ArrayList;
import java.util.HashMap;

import services.Constants;


/**
 * This class represents a course offered in the institute (eg. CS1200)
 */
public class Course{ //Note that the terms 'course number' and 'courseNumber' have been used interchangeably. They both refer to the course number like CS2200.
	private String courseNumber; // Note that this name is a modified version of the original course number. It has $inside or $outside appended to it. This is the field that has to be used for all operations except while finally printing the output. If you want the original name of the course, read the This is a Read only field.
	private int credits; //The number of credits of the course. Read only.
	private int capacity; //The number of students the course can accomodate. Read only.
	public int leastPreferredAllottedStudent; //Of the students who were allotted the course, the preference number of the student who is the least preferred by the course.
	public int capacityStillFree; //The capacity left in the course. Will be modified during the course of the algorithm as the students get allocated to it. Can be modified.
	public ArrayList<CoursePreference> coursePreferenceList; //The courses preference list over students. Read only.
	public ArrayList<CoursePreference> currentIterationStudentAllottedList; //The students allotted to the course during the algorithm. Can be modified anyhow, as is required by the algorithm
	public int noOfRejections; //keeps track of the number of rejections in the algorithm. It is used as a statistic to determine popular courses
	
	//constructor simply initializes the fields.
	public Course (String inp_courseNumber,int inp_capacity, int inp_credits){
		courseNumber=inp_courseNumber;
		credits=inp_credits;
		capacity = inp_capacity;
		capacityStillFree = capacity;
		noOfRejections = 0;
		leastPreferredAllottedStudent = 0;
		coursePreferenceList = new ArrayList<CoursePreference>();
	}
	
	/**
	 * Getter methods for private fields. No setter methods because they are intended to be read only
	 */
	public String getcourseNumber(){
		return courseNumber;
	}
	public int getCredits(){
		return credits;
	}
	public int getCapacity(){
		return capacity;
	}
	
	/**
	 * 
	 * @param inp_courseNumber
	 * @param courseList
	 * This function searches the courseList for a course with courseNumber='inp_courseNumber' and return the course object
	 */
	public static Course getCourseBycourseNumber(String inp_courseNumber,ArrayList<Course> courseList){
		for (Course tempCourse : courseList){
			if (tempCourse == null){
				System.out.println("course in course List is null, as seen in the getCourseBycourseNumber function. Exiting");
				System.exit(1);
			}
			else {
				if (tempCourse.courseNumber.compareTo(inp_courseNumber)==0){
					return tempCourse;
				}
					
			}
		}
		return null; //course not found. 
		//Remember to account for the possibility of this function returning null, else you could get a null pointer exception
	}
	
}
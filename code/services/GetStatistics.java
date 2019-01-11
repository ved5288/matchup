package services;

import java.util.ArrayList;
import java.util.Collections;

import models.Course;
import models.Student;
import models.StudentPreference;
import models.StudentClass;

/**
 *This file contains all statistics related functions. It is mainly used for computing statistics
 *pertaining to an allotment this is completed
 */
public class GetStatistics{
   
	//Self explanatory
	public static double getMean(ArrayList<Double> array) {
		double total=0;
		double size = (double) array.size();
		for (double element : array){
			total += element;
		}
		return (total/size);
	}

	//Self explanatory
	public static double getStandardDeviation(ArrayList<Double> array) {
		double varianceTotal = 0;
		double size = (double) array.size();
		double mean = getMean(array);
		double variance;
		
		for (double element : array){
			varianceTotal += (element-mean)*(element-mean);
		}
		variance = varianceTotal/size;
		return Math.sqrt(variance);
	}

	//Self explanatory
	public static double getLowest10Percentile(ArrayList<Double> array) {
		//Copy the array first because you do not want to change the original array
		ArrayList<Double> arrayCopy = copyArrayList(array);
		//Sort it
		Collections.sort(arrayCopy);
		//Compute lowest 10 percentile
		double size = arrayCopy.size();
		int tenPercentileIndex = (int) (0.1*size);
		return arrayCopy.get(tenPercentileIndex);
	}

	//Self explanatory
	public static double getHighest10Percentile(ArrayList<Double> array) {
		//Copy the array first because you do not want to change the original array
		ArrayList<Double> arrayCopy = copyArrayList(array);
		//Sort it
		Collections.sort(arrayCopy);
		//Compute lowest 10 percentile
		double size = arrayCopy.size();
		int tenPercentileIndex = (int) (0.9*size);
		return arrayCopy.get(tenPercentileIndex);
	}
	
	/**
	 * Computes some per student statistics based on the allotted courses and stores it in the student object itself
	 * @param studentList
	 */
	public static void computePerStudentStatistics(ArrayList<Student> studentList){
		computeEffectiveAverageRanks(studentList);
		computeCreditSatisfactionRatios(studentList);
	}
	
	/**
	 * Computes the credit satisfaction ratio for each student, which is (Allotted Credits)/(Max Elective Credits)
	 * Read code documentation for more details
	 * @param studentList
	 */
    private static void computeCreditSatisfactionRatios(ArrayList<Student> studentList) {
		for (Student s : studentList){
			if (s.getMaxElectiveCreditsInSem()==0 || s.studentPreferenceList.size()==0){ //If the student had no capacity. Avoids a divide by 0 in the else part
    			s.creditSatisfactionRatio = 1;
    		}
			else{
				int totalAllottedCredits = 0;
				for (StudentPreference sp : s.orderedListOfcoursesAllotted){
					totalAllottedCredits += sp.getCourseObj().getCredits();
				}
				s.creditSatisfactionRatio = (double)(totalAllottedCredits) / (double)(s.getMaxElectiveCreditsInSem());
			}
		}
	}

    
    /**
     * Computes the 'Effective Average Rank' statistic for every student
     * Effective Average Rank = (Sum of Reduced Ranks)/(No of allotted courses)^2
     * 
     * Sum of reduced ranks - each course's rank is its own rank minus the number of courses above it in the preference
     * list that were removed due to some kind of conflict.
     * 
     * Read code documentation for more details
     * @param studentList
     */
	private static void computeEffectiveAverageRanks(ArrayList<Student> studentList){
    	for (Student s : studentList){
    		if (s.orderedListOfcoursesAllotted.size()==0){ //If nothing was allotted. Avoids a divide by 0 in the else part
    			s.effectiveAverageRank = 0;
    		}
    		else{
    			//Just a copy of the original preference list since we do not want to modify it
        		ArrayList<StudentPreference> preferenceList = copyArrayList(s.studentPreferenceList);
        		//A set of courses that were allotted upto some rank i. We need to use it to remove clashing courses further down
        		ArrayList<StudentPreference> allottedCoursesSoFar = new ArrayList<StudentPreference>();
        		
        		double totalRank = 0; //Is the sum of the ranks of courses allotted to a student
        		int index = 1; //This is the the effective rank of a preference. We will account for the inside-outside department courses as 1. Also we will not count the conflicting courses
        		
        		//Looping through the student's preference list
        		for (int i=0;i<preferenceList.size();i++){
    				if (s.orderedListOfcoursesAllotted.contains(preferenceList.get(i))){ //If the student was allotted that preference
        				totalRank += index; //compute the new totalRank
        				index++; //Increment the index for the next course on the preference list to be counted
        				allottedCoursesSoFar.add(preferenceList.get(i));
        				i++; //Increment the value of i since we want to skip the outside department version of the course, because the student already got allotted to the inside department version of it
        			} else {//If the student was not allotted this preference
    					if (constrained(preferenceList.get(i),allottedCoursesSoFar,s.getMaxElectiveCreditsInSem(),s.studentClasses)){ //Check if the preference clashes with a higher preference for some reason class constraint or credits getting over
        					;//Do nothing. Index should not get incremented in this case
        				}
    					else{
    						index++;
    					}
    				}
        		}
        		s.effectiveAverageRank = totalRank/ (double)(s.orderedListOfcoursesAllotted.size()*s.orderedListOfcoursesAllotted.size()); //self explanatory formula
    		}
    	}
    }
	
	/* HELPER FUNCTIONS */
	
	/*
	 * Tells you if a given studentPreference in constrained because of any of the allotted courses in terms of
	 * a) no more scope in class
	 * b) insufficient credits left due to the set of allotted courses
	 */
	private static boolean constrained(StudentPreference preference,ArrayList<StudentPreference> allottedCoursesSoFar, int maxElectiveCreditsInSem, ArrayList<StudentClass> studentClasses) {
		
		// Check for class constraint
		for (StudentClass sc : studentClasses) {
			if(sc.coursesInThisClass.contains(preference)){
				// If this student class has this course in it check if the class constraint got satisfied by other courses.
				int numCoursesAllottedFromThisClass = 0;
				for (StudentPreference sp : allottedCoursesSoFar ) {
					if(sc.coursesInThisClass.contains(sp)){
						numCoursesAllottedFromThisClass++;
					}
				}
				if(sc.getMaxCoursesToBeAllotted()==numCoursesAllottedFromThisClass){
					return true;
				}
			}
		}
		
		//Check if total number of credits will overshoot if we allot the preference
		int allottedCredits = 0;
		for (StudentPreference sp : allottedCoursesSoFar){
			allottedCredits += sp.getCourseObj().getCredits();
		}
		if (preference.getCourseObj().getCredits() > maxElectiveCreditsInSem - allottedCredits){
			return true;
		}
		
		//No constraints found. Return false
		return false;
	}

	//Just a helper function used to do a deep copy
	private static <T> ArrayList<T> copyArrayList (ArrayList<T> inputList){
		ArrayList<T> copiedList = new ArrayList<T>();
		
		for (int i=0;i<inputList.size();i++){
			copiedList.add(inputList.get(i));
		}
		return copiedList;		
	}

	
}
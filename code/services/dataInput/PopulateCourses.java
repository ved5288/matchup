package services.dataInput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import models.Course;
import services.Constants;

public class PopulateCourses{
	/**
	 * This function reads the data from the file and creates the course objects.
	 * It takes the input file as an argument
	 * @param slots 
	 * @param errorMsgList 
	 */
	public static ArrayList<Course> execute(String inputFile, String[] errorMsgList){		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		ArrayList<Course> courseList = new ArrayList<Course>();
		Course course;
		
		//reading input line by line and adding a new course for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {			
				line.replaceAll("\\s+",""); //Remove all whitespace
				inputLine = line.split(splitBy);
				
				//Compute all the parameters required to invoke the 'new Course()' constructor
				String courseNumber = inputLine[0];
				int totalCapacity = Integer.parseInt(inputLine[1]);
				int credits = Integer.parseInt(inputLine[2]);
				
				//create the new course object 
				course = new Course(courseNumber,totalCapacity,credits);
				courseList.add(course);
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return courseList; 
	}

}
package services.dataInput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;

public class PopulateStudents{
	/**
	 * This function reads the data from the file and creates the student objects.
	 * It takes the input file as an argument
	 * @param deptSpecificMaxCreditLimitInfo 
	 */
	public static ArrayList<Student> execute(String inputFile){		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		ArrayList<Student> studentList = new ArrayList<Student>();
		Student newStudent;

		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {	
				line.replaceAll("\\s+",""); //Remove all whitespace
				inputLine = line.split(splitBy);
				//create the new student object and add to student list
				newStudent = new Student(inputLine[0],Integer.parseInt(inputLine[1]));
				studentList.add(newStudent);
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return studentList; 				
	}
}
package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * This class reads data from the input file and ensures that it is in the correct format
 * 
 * ALL FILE FORMATS ARE GIVEN IN THE README AND CODE DOCUMENTATION
 *
 */
public class CheckInputFormats {
	/** Self explanatory */
	public static String checkStudentListFileFormat(String inputFile){		
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		int lineNo=1; //Start with 1 because we want to skip the header line
		String error = null;

		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {	
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);
				//There should be exactly 2 columns
				if (inputLine.length!=2){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have 2 columns";
					break;
				}
				//First column is a string and can be anything. 
				String integerPattern = "([0-9]*)";   
				//Second column is an integer
				
				if (!Pattern.matches(integerPattern, inputLine[1])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Third column should be an integer like '4'";
					break;
				}
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error; 		
	}
	
	/** Self explanatory */
	public static String checkCourseListFileFormat(String inputFile){		
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		String error = null;
		int lineNo=1; //Start with 1 because we want to skip the header line
		
		//reading input line by line and adding a new course for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {			
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);

				//There should be exactly 2 columns
				if (inputLine.length!=3){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have 3 columns";
					break;
				}
				//First columns is a string and can be anything. Second column is an integer
				String integerPattern = "([0-9]*)";   
				if (!Pattern.matches(integerPattern, inputLine[1])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Second column should be an integer.";
					break;
				}
				
				//Third column is an integer
				if (!Pattern.matches(integerPattern, inputLine[2])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Third column should be an integer.";
					break;
				}
				
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error; 
	}
	
	/** Self explanatory */
	public static String checkStudentPreferenceListFileFormat(String inputFile){		
		return checkForAtleastXcolumnsInFile(inputFile,1); //There should be at least one column in each line 	
	}
	
	/** Self explanatory */
	public static String checkCoursePreferenceListFileFormat(String inputFile){		
		return checkForAtleastXcolumnsInFile(inputFile,1); //There should be at least one column in each line 		
	}

	/** Self explanatory */
	public static String checkStudentClassFileFormat(String inputFile){		
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		String error = null;
		int lineNo=1; //Start with 1 because we want to skip the header line
		
		//reading input line by line and adding a new course for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {			
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);

				//There should be at least 3 columns
				if (inputLine.length<3){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have at least 3 columns";
					break;
				}
				//First columns is a string and can be anything. Second column is an integer
				String integerPattern = "([0-9]*)";   
				if (!Pattern.matches(integerPattern, inputLine[1])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Second column should be an integer.";
					break;
				}
				
				//Third column onwards, all are strings so we do not care
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error; 
	}

	/** Self explanatory */
	public static String checkMasterClassFileFormat(String inputFile){		
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		String error = null;
		int lineNo=1; //Start with 1 because we want to skip the header line
		
		//reading input line by line and adding a new course for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {			
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);

				//There should be at least 2 columns
				if (inputLine.length<2){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have at least 2 columns";
					break;
				}
				//First columns is an integer
				String integerPattern = "([0-9]*)";   
				if (!Pattern.matches(integerPattern, inputLine[0])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Second column should be an integer.";
					break;
				}
				
				//Second column onwards, all are strings so we do not care
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error; 
	}


	
	/** A function that opens the file, checks that it has at least 2 columns 
	 * and then shuts it
	 * @param inputFile
	 * @return Errors found in the file
	 */
	private static String checkForAtleastXcolumnsInFile(String inputFile, int x) {
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
			
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		int lineNo=1; //Start with 1 because we want to skip the header line
		String error = null;

		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {	
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);
				//There should be atleast 2 columns
				if (inputLine.length<x){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have atleast " + x + " column(s)";
					break;
				}
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error; 
	}
}

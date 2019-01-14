package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import models.Course;
import models.Student;
import services.CheckInputFormats;
import services.CommonAlgorithmUtilities;
import services.Constants;
import services.ExchangeUnstablePairs;
import services.UnstablePairs;
import services.FirstPreferenceAllotmentAlgorithm;
import services.GetStatistics;
import services.InputSanitization;
import services.IterativeHRalgorithm;
import services.MakeInputListsUnmodifiable;
import services.OutputSanitization;
import services.ReasonsForNotAllottingPreferences;
import services.dataOutput.*;
import services.dataInput.*;

/*
 * This is the function where the execution of the program starts
 */
public class ExecuteStepsForAllotment {
		
	public static void executeAllotmentSteps(String studentListFile, String courseListFile, String studentPreferenceListFile, String coursePreferenceListFile, String masterClassFile, String studentClassFile, int algorithm, String outputFolder) throws IOException {
		
		//Some declarations
		ArrayList<Student> studentList;
		ArrayList<Student> originalStudentList;//Is a deep copy(hence modifications to student list will not modify it) of the studentList
		ArrayList<Course> courseList;
		String errorMsg;
        
        //Creating the output folder if it doesn't exist
        new File(outputFolder).mkdir();
        
      	/* CHECKING FILE FORMATS FOR INPUT DATA (to ensure that the file reading doesn't face any error)*/
        printProgressNotification("Checking file formats of Input files");
        
		//Check student list file format
		errorMsg = CheckInputFormats.checkStudentListFileFormat(studentListFile);
		if (errorMsg!=null){
			printMessage("Exiting due to error in the format of the studentList file. " + errorMsg);
			System.exit(1);
		}
		
		//Checking course list file fomat
		errorMsg = CheckInputFormats.checkCourseListFileFormat(courseListFile);
		if (errorMsg!=null){
			printMessage("Exiting due to error in the format of the course list file. " + errorMsg);
			System.exit(1);
		}
		
		//Checking the student preference list file format
		errorMsg = CheckInputFormats.checkStudentPreferenceListFileFormat(studentPreferenceListFile);
		if (errorMsg!=null){
			printMessage("Exiting due to error in the format of the student preference list file. " + errorMsg);
			System.exit(1);
		}
		
		errorMsg = CheckInputFormats.checkCoursePreferenceListFileFormat(coursePreferenceListFile);
		if (errorMsg!=null){
			printMessage("Exiting due to error in the format of the course preference list file. " + errorMsg);
			System.exit(1);
		}

		errorMsg = CheckInputFormats.checkMasterClassFileFormat(masterClassFile);
		if (errorMsg!=null){
			printMessage("Exiting due to error in the format of the student class specifications file. " + errorMsg);
			System.exit(1);
		}	

		errorMsg = CheckInputFormats.checkStudentClassFileFormat(studentClassFile);
		if (errorMsg!=null){
			printMessage("Exiting due to error in the format of the student class specifications file. " + errorMsg);
			System.exit(1);
		}	
		
		/* DATA INPUT */
		printProgressNotification("Populating data.......");        
		
		
		//Read the input course list
		String [] errorMsgList = new String[1]; //We are using an array for the error since we want to simulate a pass by reference for the errorMsgList argument
		courseList = PopulateCourses.execute(courseListFile,errorMsgList);
		if (courseList==null){ //There was some error. That is why it is null
			printMessage("Exiting. " + errorMsgList[0]);
			System.exit(1);
		}
        		
		//Read the student list data
		studentList = PopulateStudents.execute(studentListFile);
		//Make a deep copy of the student list (since it may get modified in the Input Sanitization)
		originalStudentList = CommonAlgorithmUtilities.copyArrayList(studentList); 

		
		//Read the input student preference lists
		errorMsg = PopulateStudentPreferenceList.execute(studentList,courseList,studentPreferenceListFile);
		if (errorMsg!=null){
			printMessage("Exiting due to error in student preferences file. " + errorMsg);
			System.exit(1);
		}
		
		//Read the input course preference list
		errorMsg = PopulateCoursePreferenceList.execute(studentList, courseList,coursePreferenceListFile);
		if (errorMsg!=null){
			printMessage("Exiting. " + errorMsg);
			System.exit(1);
		}
		
		//Read the input student class specifications
		errorMsg = PopulateMasterClassSpecifications.execute(studentList, masterClassFile);
		if (errorMsg!=null){
			printMessage("Exiting. " + errorMsg);
			System.exit(1);
		}

		//Read the input student class specifications
		errorMsg = PopulateStudentClassSpecifications.execute(studentList, studentClassFile);
		if (errorMsg!=null){
			printMessage("Exiting. " + errorMsg);
			System.exit(1);
		}
		


		/* INPUT SANITIZATION */
        printProgressNotification("Sanitizing Input......");
        errorMsg = InputSanitization.sanitize(studentList, courseList);
        if (errorMsg==null){ //If there were no errors
			PrintInputDataErrorLog.execute("No errors",outputFolder + "/" + Constants.inputDataErrorLogFileName);
		}
		else{ //Else if there was an error, log it
			printMessage("Input Data has errors. Error log printed to the file 'InputDataErrorLog'");
			PrintInputDataErrorLog.execute(errorMsg,outputFolder + "/" + Constants.inputDataErrorLogFileName);
		}
		
		
        /* RUN THE MAIN ALGORITHM */
        if(algorithm==1){
        	printProgressNotification("Running the Iterative HR algorithm......");
            IterativeHRalgorithm.runAlgorithm(studentList,courseList);
        }
        else if(algorithm==2){
        	printProgressNotification("Running the First Preference Allotment algorithm......");
            FirstPreferenceAllotmentAlgorithm.runAlgorithm(studentList,courseList);
        }
        else{
        	printProgressNotification("ERROR : Input for Algorithm number was incorrect. Exiting");
            System.exit(1);
        }

        
        /* OUTPUT SANITIZATION */
        printProgressNotification("Sanitizing Output ....");
        errorMsg = OutputSanitization.sanitize(studentList, courseList);
        if (errorMsg!=null){
        	printMessage("Output Sanitization : Exiting. " + errorMsg);
			System.exit(1);
		}
        
        
        /* COMPUTE STATISTICS */
        GetStatistics.computePerStudentStatistics(studentList);
        
        /* COMPUTE THE REASONS FOR ALL THE POSSIBLE STUDENT-COURSE ALLOTMENTS THAT WERE NOT MADE*/
        ReasonsForNotAllottingPreferences.computeReasonsonsForNotAllottingPreferences(originalStudentList);
        
        /* COMPUTE THE LIST OF EXCHANGE UNSTABLE PAIRS and UNSTABLE PAIRS */
        String exchangeUnstablePairs = ExchangeUnstablePairs.computeExchangeUnstablePairs(studentList);
        String unstablePairs = UnstablePairs.computeUnstablePairs(studentList);
       
        /* PRINT OUTPUT */
        printProgressNotification("Printing Output ....");
        //Write the output pairs of student-course
        PrintOutput.execute(studentList,outputFolder + "/output.csv");
        //Write the per student statistics
        PrintPerStudentStatistics.execute(studentList,outputFolder + "/perStudentStatistics.csv");
        //Write the aggregate statistics
        PrintAggregateStatistics.execute(studentList,outputFolder + "/aggregateStatistics.csv");
        //Write the number of rejections statistic
        PrintRejections.execute(courseList,outputFolder + "/rejections.csv");
        //Write the list of student preferences that have capacity 0. Not compulsory. Just so that if we feel like the course could be taken by SEAT students, we can do something about it.*/
        PrintPreferencesWithZeroCapacity.execute(studentList,outputFolder + "/preferencesWithZeroCapacity.csv");
        //Write the set of courses allotted for each student
        PrintPerStudentAllottedCourses.execute(studentList,outputFolder + "/perStudentAllottedCourses.csv");
        //Write the set of students allotted to each course
        PrintPerCourseAllottedStudents.execute(studentList,courseList,outputFolder + "/perCourseAllotedStudents.csv");
        //Write the reason for every student-course pair that was not allotted
        PrintReasonsForNotAllottingPreferences.execute(studentList,outputFolder + "/reasonsForNotAllottingPreferences.csv");
      	//Write the reason for every student-course pair that was not allotted
        CreateFolderForStudentEmails.execute(studentList,outputFolder+"/studentEmails");
        //Write out the set of exchange unstable pairs for this allotment
        PrintExchangeUnstablePairs.execute(exchangeUnstablePairs,outputFolder + "/exchangeUnstablePairs.csv");
        //Write out the set of unstable pairs for this allotment
        PrintExchangeUnstablePairs.execute(unstablePairs,outputFolder + "/unstablePairs.csv");
    }
	
	//Just a function which sends the message to be printed to the correct output
	public static void printMessage(String s){
		Main.displayMessage(s);
	}
	
	//Just a function which sends the progress notification to be printed to the correct output
	//The progress notification just notifies how many steps of the allotment have been completed
	public static void printProgressNotification(String s){
		Main.printProgress(s);
	}
}

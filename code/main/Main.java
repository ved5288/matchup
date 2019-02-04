package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * This function is responsible for taking all the inputs from the user from the terminal,
 * and it also prints all the required information to the terminal.
 * 
 * This class is not supposed to do anything other than text input/output
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {

		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

		Scanner sc = new Scanner (System.in);

		/* TAKING USER INPUT AND SENDING IT TO THE EXECUTE STEPS FOR ALLOTMENT FUNCTION*/
		
		//Read student list file
		System.out.println("Please enter the input csv file name where the list of students+CGPAs in the above format is stored");
		String studentListFile = sc.nextLine();
		
		//Read course list file
		System.out.println("Please enter the input csv file name where the list of courses+rankingCriteria+capacity in the above format is stored");
		String courseListFile = sc.nextLine();
		
		//Read student preference list file
		System.out.println("Please enter the input csv file name where the preference list in the above format are stored");
		String studentPreferenceListFile = sc.nextLine();
				
		System.out.println("Please enter the location of file from where you want the course preferences to be read: ");
		String coursePreferenceListFile = sc.nextLine();

		System.out.println("Please enter the location of file from where you want the master class constraint specifications to be read: ");
		String masterClassFile = sc.nextLine();
	
		System.out.println("Please enter the location of file from where you want the student class constraint specifications to be read: ");
		String studentClassFile = sc.nextLine();

		//Reading which algorithm to use
		System.out.println("Which algorithm to run?");
        System.out.println("1. Iterative HR");
        System.out.println("2. First Preference Allotment");
        System.out.println("3. Load Max Cardinality Matching");
        int algorithm = sc.nextInt();
        sc.nextLine(); //This is necessary because the sc.nextInt() doesn't consume the '\n' and this is read by the sc.nextLine() appearing after this. Hence this is just meant to consume the '\n' character
		
        String outputFile="";
        if(algorithm==3){
      		System.out.println("Please enter the location of file from where you want the output to be read: ");
			outputFile = sc.nextLine();  	
        }

        //Reading the folder to which the output is to be printed
        System.out.println("Please enter output folder to print to");
        String outputFolder = sc.nextLine();
        
      	//Call the executeAllotmentSteps function to run the allotment
      	ExecuteStepsForAllotment.executeAllotmentSteps(studentListFile,courseListFile,studentPreferenceListFile,coursePreferenceListFile,masterClassFile,studentClassFile,algorithm,outputFile,outputFolder);
      	
      	//Program has ended
      	System.out.println("Execution over.......");
      	sc.close();
	}
	
	public static void displayMessage(String s){
		System.out.println(s);
	}

	public static void printProgress(String s){
		System.out.println(s);
	}
}

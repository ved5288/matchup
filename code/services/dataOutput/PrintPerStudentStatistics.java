package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;

public class PrintPerStudentStatistics{
	/**
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
	 * Outputs the the 'Effective Average Rank' and 'Credit Satisfaction Ratio' for each student
	 */
	public static void execute(ArrayList<Student> studentList, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		//Header line for file
		bw.write("Student Roll No,EffectiveAverageRank,creditSatisfactionRatio\n");

		//Looping through the students and writing each one's allotted courses
		for (Student s : studentList){
			bw.write(s.getRollNo());
			bw.write(",");
			bw.write(Double.toString(roundOff(s.effectiveAverageRank)));
			bw.write(",");
			bw.write(Double.toString(roundOff(s.creditSatisfactionRatio)));
			bw.write("\n");
		}
		bw.close();
		
	}

	//Helper Function to round off to 2 decimal places
	public static double roundOff(double noOfRejectionsRatio){
		return (double)Math.round(noOfRejectionsRatio * 100) / 100;
	}
	

}
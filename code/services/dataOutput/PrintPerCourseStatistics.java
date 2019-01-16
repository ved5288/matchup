package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Course;

public class PrintPerCourseStatistics{
	/**
	 * @param courseList
	 * @param outputFile
	 * @throws IOException
	 * Outputs the the 'Effective Average Rank' and 'Credit Satisfaction Ratio' for each course
	 */
	public static void execute(ArrayList<Course> courseList, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		//Header line for file
		bw.write("CourseNumber,EffectiveAverageRank,capacitySatisfactionRatio\n");

		//Looping through the courses and writing each one's statistics
		for (Course c : courseList){
			bw.write(c.getcourseNumber());
			bw.write(",");
			bw.write(Double.toString(roundOff(c.effectiveAverageRank)));
			bw.write(",");
			bw.write(Double.toString(roundOff(c.capacitySatisfactionRatio)));
			bw.write("\n");
		}
		bw.close();
		
	}

	//Helper Function to round off to 2 decimal places
	public static double roundOff(double noOfRejectionsRatio){
		return (double)Math.round(noOfRejectionsRatio * 100) / 100;
	}
	

}
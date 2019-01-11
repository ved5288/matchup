package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Course;

public class PrintRejections{
	/**
	 * Prints the number of students rejected by a course after it got full. This is a heuristic to measure
	 * the popularity of a course
	 * @param courseList
	 * @param outputFile
	 * @throws IOException
	 */
	public static void execute(ArrayList<Course> courseList,String outputFile) throws IOException{
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Writing the header line
		bw.write("Course ID");
		bw.write(",");
		bw.write("Number of Rejections");
		bw.write(",");
		bw.write("Course Capacity");
		bw.write(",");
		bw.write("Number of Rejections / Capacity");
		bw.write("\n");
		
		//Filling up the table
		for (int i =0;i<courseList.size();i++){
			bw.write(courseList.get(i).getcourseNumber()); //Using 'courseNumber' instead of 'courseNumberToPrint' because it gives us a split up of rejections in the inside and outside department versions of the course
			bw.write(",");
			bw.write(Integer.toString(courseList.get(i).noOfRejections));
			bw.write(",");
			bw.write(Integer.toString(courseList.get(i).getCapacity()));
			bw.write(",");
			//Calculate the rejections ratios
			double noOfRejectionsRatio;
			if (courseList.get(i).getCapacity()==0){
				noOfRejectionsRatio = 0;
			}
			else{
				noOfRejectionsRatio = (double)(courseList.get(i).noOfRejections) / (double)(courseList.get(i).getCapacity());
				//Round off the ratio
				noOfRejectionsRatio = roundOff(noOfRejectionsRatio);
			}
			//Writing the rejections ratios
			bw.write(Double.toString(noOfRejectionsRatio));
			bw.write("\n");
		}
		bw.close();
	}
	
	//Helper Function to round off to 2 decimal places
	public static double roundOff(double noOfRejectionsRatio){
		return (double)Math.round(noOfRejectionsRatio * 100) / 100;
	}
	
}
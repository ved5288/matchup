package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;
import models.StudentPreference;
import models.Course;
import services.GetStatistics;

public class PrintAggregateStatistics{

	/**
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
	 * Outputs aggregate statistics like mean,variance,10 percentile of the 'Effective Average Rank' and 'Credit Satisfaction Ratio' for each student
	 */
	public static void execute(ArrayList<Student> studentList, ArrayList<Course> courseList, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		ArrayList<Double> studentEffectiveAvgRanks = new ArrayList<Double>();
		ArrayList<Double> creditSatisfactionRatios = new ArrayList<Double>();
		for (Student s : studentList){
			studentEffectiveAvgRanks.add(s.effectiveAverageRank);
			creditSatisfactionRatios.add(s.creditSatisfactionRatio);
		}
			
		ArrayList<Double> courseEffectiveAvgRanks = new ArrayList<Double>();
		ArrayList<Double> capacitySatisfactionRatios = new ArrayList<Double>();
		for (Course c : courseList){
			courseEffectiveAvgRanks.add(c.effectiveAverageRank);
			capacitySatisfactionRatios.add(c.capacitySatisfactionRatio);
		}


		bw.write("MEAR-S,");
		double studentEffectiveRankMean = GetStatistics.getMean(studentEffectiveAvgRanks);
		bw.write(Double.toString(roundOff(studentEffectiveRankMean)));
		bw.write("\nMEAR-C,");
		double courseEffectiveRankMean = GetStatistics.getMean(courseEffectiveAvgRanks);
		bw.write(Double.toString(roundOff(courseEffectiveRankMean)));
		bw.write("\nCS-S,");
		double creditSatisfactionMean = GetStatistics.getMean(creditSatisfactionRatios);
		bw.write(Double.toString(roundOff(creditSatisfactionMean)));
		bw.write("\nCS-C,");
		double capacitySatisfactionMean = GetStatistics.getMean(capacitySatisfactionRatios);
		bw.write(Double.toString(roundOff(capacitySatisfactionMean)));
		
		int sizeOfMatching = 0;
		int totalPreferences = 0;
		int totalCredits = 0;
		int totalClasses = 0;
		for (Student s : studentList ) {
			sizeOfMatching += s.orderedListOfcoursesAllotted.size();
			totalPreferences += s.studentPreferenceList.size();
			for (StudentPreference sp : s.orderedListOfcoursesAllotted ) {
				totalCredits += sp.getCourseObj().getCredits();
			}
			totalClasses += s.studentClasses.size(); 
		}
		double averageClasses = (double) totalClasses / (double) studentList.size();

		bw.write("\nTP,");
		bw.write(Integer.toString(totalPreferences));
		bw.write("\nSM,");
		bw.write(Integer.toString(sizeOfMatching));
		bw.write("\nTC,");
		bw.write(Integer.toString(totalCredits));
		bw.write("\nAC,");
		bw.write(Double.toString(roundOff(averageClasses)));
		
		bw.close();
		
	}

	//Helper Function to round off to 2 decimal places
	public static double roundOff(double noOfRejectionsRatio){
		return (double)Math.round(noOfRejectionsRatio * 100) / 100;
	}
	
	
}
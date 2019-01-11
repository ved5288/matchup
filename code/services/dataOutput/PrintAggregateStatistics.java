package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;
import services.GetStatistics;

public class PrintAggregateStatistics{

	/**
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
	 * Outputs aggregate statistics like mean,variance,10 percentile of the 'Effective Average Rank' and 'Credit Satisfaction Ratio' for each student
	 */
	public static void execute(ArrayList<Student> studentList,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		ArrayList<Double> effectiveAvgRanks = new ArrayList<Double>();
		ArrayList<Double> creditSatisfactionRatios = new ArrayList<Double>();
		for (Student s : studentList){
			effectiveAvgRanks.add(s.effectiveAverageRank);
			creditSatisfactionRatios.add(s.creditSatisfactionRatio);
		}
		
		bw.write("EFFECTIVE RANK STATISTICS");
		bw.write("\n Mean = ");
		double effectiveRankMean = GetStatistics.getMean(effectiveAvgRanks);
		bw.write(Double.toString(roundOff(effectiveRankMean)));
		bw.write("\n Standard Deviation = ");
		double effectiveRankSd = GetStatistics.getStandardDeviation(effectiveAvgRanks);
		bw.write(Double.toString(roundOff(effectiveRankSd)));
		bw.write("\n Lowest 10Percentile = ");
		double effectiveRank10Percentile = GetStatistics.getHighest10Percentile(effectiveAvgRanks);
		bw.write(Double.toString(roundOff(effectiveRank10Percentile)));
		
		bw.write("\n\nCREDIT SATISFACTION RATIO STATISTICS");
		bw.write("\n Mean = ");
		double creditSatisfactionMean = GetStatistics.getMean(creditSatisfactionRatios);
		bw.write(Double.toString(roundOff(creditSatisfactionMean)));
		bw.write("\n Standard Deviation = ");
		double creditSatisfactionSd = GetStatistics.getStandardDeviation(creditSatisfactionRatios);
		bw.write(Double.toString(roundOff(creditSatisfactionSd)));
		bw.write("\n Lowest 10Percentile = ");
		double creditSatisfaction10Percentile = GetStatistics.getLowest10Percentile(creditSatisfactionRatios);
		bw.write(Double.toString(roundOff(creditSatisfaction10Percentile)));
		
		bw.close();
		
	}

	//Helper Function to round off to 2 decimal places
	public static double roundOff(double noOfRejectionsRatio){
		return (double)Math.round(noOfRejectionsRatio * 100) / 100;
	}
	
	
}
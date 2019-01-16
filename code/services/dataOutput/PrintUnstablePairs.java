package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PrintUnstablePairs{
	public static void execute(String unstablePairs,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
					
		//Header line for file
		bw.write("RollNo,Course,LeastPreferredAllottedStudentForThisCourse\n");
		bw.write(unstablePairs);
		bw.close();	
	}

}
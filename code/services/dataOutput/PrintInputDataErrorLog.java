package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PrintInputDataErrorLog{
	/* This function prints the errors found in the input data to a file. It is up to the user 
	 * to decide if the pruned errors are acceptable or not. If not, he should manually make the 
	 * required changes to the input
	 */
	public static void execute(String errorData,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(errorData);
		bw.close();
	}

}
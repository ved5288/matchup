package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PrintExchangeUnstablePairs{
	public static void execute(String exchangeUnstablePairs,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
					
		//Header line for file
		bw.write("Student1 Roll No,Student1 Allotted Course,Student2 Roll No,Student2 Allotted Course,\n");
		bw.write(exchangeUnstablePairs);
		bw.close();	
	}

}
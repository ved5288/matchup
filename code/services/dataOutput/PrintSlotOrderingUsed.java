package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Slot;

public class PrintSlotOrderingUsed{
	/* Function is used to print the slot ordering used for the SlotBasedAlgorithm. It is not
	 * used if we use any of the other algorithms
	 */
	public static void execute(ArrayList<Slot> slotOrderingUsed, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		for (Slot s : slotOrderingUsed){
			bw.write(s.getSlotName() + ",");
		}
		bw.close();
	}

}
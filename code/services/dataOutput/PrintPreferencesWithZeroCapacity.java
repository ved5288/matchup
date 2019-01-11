package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;
import models.StudentPreference;

public class PrintPreferencesWithZeroCapacity{
	/* This functions takes in the studentList and prints out those student preferences which 
	 * have zero capacity. The purpose is so that we can investigate whether the course should
	 * truly have a zero capacity, and to figure out why the student gave a preference for it.
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
		bw.write("Student Roll Number, Preference with zero capacity\n");
		
		//Looping through the students and writing each one's allotted courses
		for (Student s : studentList){
			for (StudentPreference sp : s.studentPreferenceList){
				if (sp.getCourseObj().getCapacity() == 0){
					bw.write(s.getRollNo());
			        bw.write(",");
			        bw.write(sp.getCourseObj().getcourseNumber());
			        bw.write("\n");
				}
			}
		}
		bw.close();
		
	}
}
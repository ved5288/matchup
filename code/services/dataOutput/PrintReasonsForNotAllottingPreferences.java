package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;
import models.StudentPreference;

public class PrintReasonsForNotAllottingPreferences{

	public static void execute(ArrayList<Student> studentList, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		//Header line for file
		bw.write("Student Roll No,Course Number,Reason for Not allotting Preference\n");

		//Looping through the students and writing all preferences that were not allotted
		for (Student s : studentList){
			//first write invalid preferences
			for (StudentPreference sp : s.invalidPreferences){
				bw.write(s.getRollNo());
				bw.write(",");
				bw.write(sp.getCourseObj().getcourseNumber());
				bw.write(",");
				bw.write(sp.detailedReasonForNotAllottingThisPreference);
				bw.write("\n");
			}
			
			//next, for each valid preference compute the reason why it is not allotted, in case it was not allotted
			for (StudentPreference sp : s.studentPreferenceList){
				if (!s.orderedListOfcoursesAllotted.contains(sp)){
					bw.write(s.getRollNo());
					bw.write(",");
					bw.write(sp.getCourseObj().getcourseNumber());
					bw.write(",");
					bw.write(sp.detailedReasonForNotAllottingThisPreference);
					bw.write("\n");
				}
			}
		}
		bw.close();	
	}
}
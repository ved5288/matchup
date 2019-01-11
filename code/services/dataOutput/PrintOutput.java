package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;
import models.StudentPreference;

public class PrintOutput{
	/**
	 * Prints the allotment to the output file. Each row contains a student roll number, 
	 * an allotted course number and the preference number of the student that the course was at
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
	 */
	public static void execute(ArrayList<Student> studentList,String outputFile) throws IOException{
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Header line for file
		bw.write("Student Roll Number, Course ID \n");
		
		//Looping through the students and writing each one's allotted courses
		for (Student s : studentList){
			for (StudentPreference sp : s.orderedListOfcoursesAllotted){
				bw.write(s.getRollNo());
                bw.write(",");
                bw.write(sp.getCourseObj().getcourseNumber());
				bw.write("\n");
			}
		}
		bw.close();
	}
	
}
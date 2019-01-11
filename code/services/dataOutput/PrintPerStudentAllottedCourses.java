package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;
import models.StudentPreference;

public class PrintPerStudentAllottedCourses{

	/**
	 * This students prints for every student, the set of courses allotted to that student.
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
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
		bw.write("Student Roll Number, Electives Allotted \n");
			
		//Looping through the students and writing each one's allotted courses
		for (Student s : studentList){
			bw.write(s.getRollNo());
			for (StudentPreference sp : s.orderedListOfcoursesAllotted){
		        bw.write(",");
		        bw.write(sp.getCourseObj().getcourseNumber());
			}
			bw.write("\n");
		}
		bw.close();
	}


}
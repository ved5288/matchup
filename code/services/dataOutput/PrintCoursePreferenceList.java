package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Course;
import models.CoursePreference;

public class PrintCoursePreferenceList{
	
	/* This function takes in the courseList and prints the course preference lists */
	public static void execute(ArrayList<Course> courseList,String outputFile) throws IOException{
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Write the header line
		bw.write("Course number,Preferences \n");
		
		//Loop through the courses
		for (Course c : courseList){
			bw.write(c.getcourseNumber());
			//Write this course preference list to the file
        	for (CoursePreference cp : c.coursePreferenceList){
        		bw.write("," + cp.getStudentObj().getRollNo());
        	}
        	bw.write("\n");
		}
    	bw.close();
	}
	
}
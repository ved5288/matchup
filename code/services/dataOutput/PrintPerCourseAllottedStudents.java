package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Course;
import models.Student;
import models.StudentPreference;

public class PrintPerCourseAllottedStudents{
	/**
	 * This function prints for every course, the set of students allotted to that course
	 * @param studentList
	 * @param courseList
	 * @param outputFile
	 * @throws IOException
	 */
	public static void execute(ArrayList<Student> studentList, ArrayList<Course> courseList,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Header line for file
		bw.write("Course ID, Total Capacity, Allotted Capacity, Students Allotted\n");
		//Loop through the courses and write the unfulfilled capacities of each course
		for (Course c : courseList){
			//Write course id
			bw.write(c.getcourseNumber());
			ArrayList<Student> studentsAllottedForThisCourse = new ArrayList<Student>();
			//Loop through the student list to find out which students were allotted to this course
			for (Student s : studentList){
				for (StudentPreference sp : s.orderedListOfcoursesAllotted){
					if (sp.getCourseObj() == c){
						studentsAllottedForThisCourse.add(s);
						break;
					}
				}
			}
			//Write the capacity of the course and the number of students allotted
			bw.write(",");
			bw.write(Integer.toString(c.getCapacity()));
			bw.write(",");
			bw.write(Integer.toString(studentsAllottedForThisCourse.size()));
			//Write the roll numbers of all the students allotted to this course
			for (Student s : studentsAllottedForThisCourse){
				bw.write(",");
				bw.write(s.getRollNo());
			}
			bw.write("\n");
		}
		bw.close();
		
	}

}
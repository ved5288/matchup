package services.dataOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Student;
import models.StudentPreference;

public class CreateFolderForStudentEmails{
	/**
	 * This function creates a new folder, in which we have 1 email message 
	 * for every student. It consists of the list of allotted courses, and 
	 * the list of courses not allotted, along with their reason. 
	 * This reason will be a simplified version of the reason in the 
	 * reasonsForNotAllottingPreferences.csv file
	 * The purpose is to be able to write an automated script to email every student
	 * information about his or her allotment
	 * @param studentList
	 * @param outputFolder
	 * @throws IOException
	 */
	public static void execute(ArrayList<Student> studentList, String outputFolder) throws IOException {
		new File(outputFolder).mkdir();

		//Looping through the students
		for (Student s : studentList){
			//Open output file for writing (name is that of the student's roll number)

			File outfile = new File(outputFolder + "/" + s.getRollNo());
			if (!outfile.exists()) {
				outfile.createNewFile();
			}
			FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
						
			bw.write("Dear Student "+s.getRollNo()+",\n \n");
			
			//If the student has not registered
			if (s.studentPreferenceList.size()==0 && s.invalidPreferences.size()==0 ){
				bw.write("From the data obtained from workflow we see that you have not registered for courses during the registration week. Owing to this, no courses have been allotted to you via SEAT.\n");
				bw.write("\nNo paper requests will be entertained.\n\n");
				//bw.write("Timeline for Round-2:\n");
				//bw.write("---------------------------------\n");
				//bw.write("Jul 30, 2018, 10am -- Aug. 1, 2018, 5pm\t: Last date for dropping courses and replacing it through SEAT\n" +
				//		"\tAug. 2, 2018, 5pm\t\t: Updating databases with vacancies in each course\n" +
				//		"Aug. 3, 2018, 10am -- Aug. 6, 2018, 5pm\t: Students see the vacancies and provide new preference list\n" +
				//		"\tAug. 8, 2018, 5pm\t\t: Second round of elective allocation done through SEAT is sent to workflow and the students\n\n");
				bw.write("Best Regards,\n");
				bw.write("SEAT Team\n");
				bw.close();
				continue;
			}
			
			//If the student has registered
			bw.write("The SEAT allocations for the Jan-May 2019 semester are done. ");
			//Write set of allotted Courses
			if (s.orderedListOfcoursesAllotted.size()==0){ //If no elective courses were allotted
				bw.write("You have not been allotted any elective course in this round of allotment. (This does not include core courses)\n");
			}
			else{ //If at least 1 elective course was allotted
				bw.write("Please find below the elective courses allotted to you. This does not include the core courses.\n\nList of courses allotted:\n");
				for (StudentPreference sp : s.orderedListOfcoursesAllotted){

					bw.write(sp.getCourseObj().getcourseNumber());
					bw.write("\n");
				}
			}
			bw.write("\n");
			
			//Write set of courses not allotted, along with reasons
			//If there are no courses in this section
			if (s.invalidPreferences.size()==0 && (s.studentPreferenceList.size()==s.orderedListOfcoursesAllotted.size())){
				if(s.studentPreferenceList.size()==0){
					bw.write("You did not give preference for any elective courses.\n");
				} else{
					bw.write("You have been allotted all your preferred courses.\n");
				}
				//bw.write("There is no elective on your preference list that has not been allotted\n");
			}
			else{
				bw.write("For your ready reference please find below the list of courses that were not allotted to you and the reason due to which SEAT was unable to allot the course to you.\n\n");
				bw.write("List of courses not allotted:\n");
				//Write set of Invalid preferences
				for (StudentPreference sp : s.invalidPreferences){
					//Student could not get allotted to the course because it was an invalid preference
				
					bw.write(sp.getCourseObj().getcourseNumber());
					bw.write(" : ");
					bw.write(sp.reasonForNotAllottingThisPreference);
					bw.write("\n");
					
				}
				
				//next, for each valid preference compute the reason why it is not allotted, in case it was not allotted
				for (StudentPreference sp : s.studentPreferenceList){
					if (!s.orderedListOfcoursesAllotted.contains(sp)){
						//If the student did not get allotted to the course
						
						bw.write(sp.getCourseObj().getcourseNumber());
						bw.write(" : ");
						bw.write(sp.reasonForNotAllottingThisPreference);
						bw.write("\n");		
					}
				}
			}

			bw.write("\nNote that there will be no Round-2 for the allocation of Jan-May 2019.\nPlease note that paper requests submitted to academic section cannot be entertained by SEAT. If you have an issue you must email to seat@wmail.iitm.ac.in with the details of your issue. \n\n");
			bw.write("Best Regards,\n");
			bw.write("SEAT Team\n");
			bw.close();
		}
	}
}

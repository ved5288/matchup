
JCC = javac
VPATH = entity:main:service
# combining all to make the final executable
seat: Main.java
	$(JCC) $(JFLAGS) */*.java
	$(JCC) $(JFLAGS) */*/*.java
	jar cfm CourseAllocationTool.jar Manifest.mf */*.class */*/*.class

#used to clean all class files
clean: 
	$(RM) */*.class
	$(RM) */*/*.class

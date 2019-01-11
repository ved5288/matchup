
JCC = javac
VPATH = entity:main:service
# combining all to make the final executable
seat: MainWithoutGUI.java
	$(JCC) $(JFLAGS) */*.java
	$(JCC) $(JFLAGS) */*/*.java
	jar cfm SEAT.jar Manifest.mf */*.class */*/*.class

#used to clean all class files
clean: 
	$(RM) */*.class

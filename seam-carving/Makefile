JFLAGS = -cp "lib/*:."
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) -g $(JFLAGS) $*.java

CLASSES = \
		  Picture.java \
		  SeamCarver.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

seam:
	java $(JFLAGS) SeamCarver

zip:
	zip SeamCarving.zip SeamCarver.java

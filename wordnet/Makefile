JFLAGS = -cp "lib/*:."
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) -g $(JFLAGS) $*.java

CLASSES = \
		  SAP.java \
		  WordNet.java \
		  Outcast.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

sap:
	java $(JFLAGS) SAP digraph1.txt

wordnet:
	java $(JFLAGS) WordNet synsets.txt hypernyms.txt

outcast:
	java $(JFLAGS) Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt

zip:
	zip wordnet.zip $(CLASSES)

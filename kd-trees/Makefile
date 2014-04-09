JFLAGS = -cp "lib/*:."
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) -g $(JFLAGS) $*.java

CLASSES = \
		  RectHV.java \
		  PointSET.java \
		  KdTree.java \
		  KdTreeVisualizer.java \
		  RangeSearchVisualizer.java \
		  NearestNeighborVisualizer.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

kdtreevisualizer:
	java $(JFLAGS) KdTreeVisualizer input100K.txt

rangesearchvisualizer:
	java $(JFLAGS) RangeSearchVisualizer input100K.txt

nearestneighborvisualizer:
	java $(JFLAGS) NearestNeighborVisualizer input100K.txt

zip:
	zip kdtree.zip PointSET.java KdTree.java

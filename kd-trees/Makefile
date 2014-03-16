JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  Point2D.java \
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

zip:
	zip kdtree.zip PointSET.java KdTree.java

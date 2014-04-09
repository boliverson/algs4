import java.util.TreeSet;
import java.util.ArrayList;

public class PointSET {

    private TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (set.contains(p)) {
            return;
        }
        set.add(p);
    }

    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    public void draw() {
        StdDraw.setXscale(0.0, 1.0);
        StdDraw.setYscale(0.0, 1.0);
        for (Point2D p : set) {
            p.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> rangeList = new ArrayList<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                rangeList.add(p);
            }
        }
        return rangeList;
    }

    public Point2D nearest(Point2D p) {
        if (set.isEmpty()) {
            return null;
        }
        double minDistance = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D point : set) {
            double distance = p.distanceSquaredTo(point);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
}

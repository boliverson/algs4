import java.util.ArrayList;
import java.util.Collections;

public class Brute {
    public static void main(String[] args) {
        In jin = new In(args[0]);
        int n = jin.readInt();
        Point[] points = new Point[n];

        for (int i = 0; i < points.length; ++i) {
            int x = jin.readInt();
            int y = jin.readInt();
            points[i] = new Point(x, y);
        }

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (int p = 0; p < points.length - 3; ++p) {
            for (int q = p + 1; q < points.length - 2; ++q) {
                double slopePQ = points[p].slopeTo(points[q]);
                for (int r = q + 1; r < n - 1; ++r) {
                    double slopePR = points[p].slopeTo(points[r]);
                    if (slopePQ != slopePR) {
                        continue;
                    }
                    for (int s = r + 1; s < n; ++s) {
                        double slopePS = points[p].slopeTo(points[s]);
                        if (slopePQ != slopePS) {
                            continue;
                        }
                        ArrayList<Point> pointList = new ArrayList<Point>();
                        pointList.add(points[p]);
                        pointList.add(points[q]);
                        pointList.add(points[r]);
                        pointList.add(points[s]);
                        Collections.sort(pointList);

                        for (int i = 0; i < pointList.size() - 1; ++i) {
                            StdOut.print(pointList.get(i));
                            StdOut.print(" -> ");
                        }
                        StdOut.print(pointList.get(pointList.size() - 1));
                        StdOut.println();

                        pointList.get(0).drawTo(pointList.get(pointList.size() - 1));
                    }
                }
            }
        }
    }
}

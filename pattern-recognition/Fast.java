import java.util.ArrayList;
import java.util.Collections;

public class Fast {

    public static void main(String[] args) {
        In jin = new In(args[0]);
        int n = jin.readInt();
        Point[] points = new Point[n];
        int[] id = new int[n];
        
        for (int i = 0; i < n; ++i) {
            int x = jin.readInt();
            int y = jin.readInt();
            points[i] = new Point(x, y);
            id[i] = i;
        }

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        boolean[] marked = new boolean[n];

        for (int p = 0; p < n; ++p) {
            if (marked[id[p]]) {
                continue;
            }
            marked[id[p]] = true;
            quickSort(id, p + 1, n - 1, points[id[p]], points);
            ArrayList<Point> pointList = new ArrayList<Point>();
            for (int q = p + 1; q < n; ++q) {
                if (marked[id[q]]) {
                    continue;
                }
                pointList.add(points[id[q]]);
            }
            ArrayList<Point> tokenList = new ArrayList<Point>();
            for (int l = 0; l < pointList.size(); ) {
                int r = l;
                while (points[id[p]].slopeTo(pointList.get())
                l = r;
            }
        }
    }

    private static void quickSort(int[] id, int lo, int hi, Point p, Point[] points) {
        if (hi <= lo) {
            return;
        }
        int i = partition(id, lo, hi, p, points);
        quickSort(id, lo, i - 1, p, points);
        quickSort(id, i + 1, hi, p, points);
    }

    private static int partition(int[] id, int lo, int hi, Point p, Point[] points) {
        int i = lo;
        int j = lo;
        int temp;
        while (j < hi) {
            if (p.SLOPE_ORDER.compare(points[id[j]], points[id[hi]]) < 0) {
                if (i != j) {
                    temp = id[i];
                    id[i] = id[j];
                    id[j] = temp;
                }
                ++i;
            }
            ++j;
        }
        temp = id[i];
        id[i] = id[hi];
        id[hi] = temp;
        return i;
    }
}

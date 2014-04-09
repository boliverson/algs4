import java.util.TreeSet;

public class KdTree {
    private class KdTreeNode {
        private Point2D point;
        private RectHV rect;
        private KdTreeNode left;
        private KdTreeNode right;

        private KdTreeNode(Point2D point, RectHV rect, KdTreeNode left, KdTreeNode right) {
            this.point = point;
            this.rect = rect;
            this.left = left;
            this.right = right;
        }
    }

    private KdTreeNode root;
    private int size;

    private Point2D nearestPoint;

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (contains(p)) {
            return;
        }
        root = insert(root, 0, p, new RectHV(0.0, 0.0, 1.0, 1.0));
        ++size;
    }

    private KdTreeNode insert(KdTreeNode node, int level, Point2D p, RectHV r) {
        if (node == null) {
            return new KdTreeNode(p, r, null, null);
        }
        if (level % 2 == 0) {
            if (p.x() < node.point.x()) {
                node.left = insert(node.left, level + 1, p, new RectHV(r.xmin(), r.ymin(), node.point.x(), r.ymax()));
            } else {
                node.right = insert(node.right, level + 1, p, new RectHV(node.point.x(), r.ymin(), r.xmax(), r.ymax()));
            }
        } else {
            if (p.y() < node.point.y()) {
                node.left = insert(node.left, level + 1, p, new RectHV(r.xmin(), r.ymin(), r.xmax(), node.point.y()));
            } else {
                node.right = insert(node.right, level + 1, p, new RectHV(r.xmin(), node.point.y(), r.xmax(), r.ymax()));
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        return contains(root, 0, p);
    }

    private boolean contains(KdTreeNode node, int level, Point2D p) {
        if (node == null) {
            return false;
        }
        if (p.equals(node.point)) {
            return true;
        }
        if (level % 2 == 0) {
            if (p.x() < node.point.x()) {
                return contains(node.left, level + 1, p);
            } else {
                return contains(node.right, level + 1, p);
            }

        } else {
            if (p.y() < node.point.y()) {
                return contains(node.left, level + 1, p);
            } else {
                return contains(node.right, level + 1, p);
            }
        }
    }

    public void draw() {
        draw(root, 0);
    }

    private void draw(KdTreeNode node, int level) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());
        StdDraw.setPenRadius();
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }
        draw(node.left, level + 1);
        draw(node.right, level + 1);
    }

    public Iterable<Point2D> range(RectHV rect) {
        return searchRange(root, 0, rect);
    }

    private TreeSet<Point2D> searchRange(KdTreeNode node, int level, RectHV r) {
        TreeSet<Point2D> set = new TreeSet<Point2D>();
        if (node == null || !r.intersects(node.rect)) {
            return set;
        }
        if (r.contains(node.point)) {
            set.add(node.point);
        }
        TreeSet<Point2D> leftSet = searchRange(node.left, level + 1, r);
        TreeSet<Point2D> rightSet = searchRange(node.right, level + 1, r);
        set.addAll(leftSet);
        set.addAll(rightSet);
        return set;
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        nearestPoint = root.point;
        searchNearest(root, 0, p);
        return nearestPoint;
    }


    private void searchNearest(KdTreeNode node, int level, Point2D p) {
        if (node == null) {
            return;
        }
        if (node.rect.distanceSquaredTo(p) >= nearestPoint.distanceSquaredTo(p)) {
            return;
        }
        if (node.point.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
            nearestPoint = node.point;
        }
        if (level % 2 == 0) {
            if (p.x() < node.point.x()) {
                searchNearest(node.left, level + 1, p);
                searchNearest(node.right, level + 1, p);
            } else {
                searchNearest(node.right, level + 1, p);
                searchNearest(node.left, level + 1, p);
            }
        } else {
            if (p.y() < node.point.y()) {
                searchNearest(node.left, level + 1, p);
                searchNearest(node.right, level + 1, p);
            } else {
                searchNearest(node.right, level + 1, p);
                searchNearest(node.left, level + 1, p);
            }
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        /*
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(.02);

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            StdDraw.show(0);
            StdDraw.show(40);
        }
        */
        while (true) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D query = new Point2D(x, y);
            Point2D nb = brute.nearest(query);
            StdOut.println("brute: " + nb);
            Point2D nk = kdtree.nearest(query);
            StdOut.println("kdtree: " + nk);
        }
    }

}

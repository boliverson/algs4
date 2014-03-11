import java.util.ArrayList;

public class KdTree {
    private class KdTreeNode {
        private Point2D point;
        private KdTreeNode left;
        private KdTreeNode right;

        private KdTreeNode(Point2D point, KdTreeNode left, KdTreeNode right) {
            this.point = point;
            this.left = left;
            this.right = right;
        }
    }

    private KdTreeNode root;
    private int size;

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
        insert(p, 0, root);
        ++size;
    }

    public boolean contains(Point2D p) {
        return contains(p, 0, root);
    }

    public void draw() {
        StdDraw.setXscale(0.0, 1.0);
        StdDraw.setYscale(0.0, 1.0);
        draw(0, root);
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> rangeList = new ArrayList<Point2D>();
        range(rect, rangeList);
        return rangeList;
    }

    private void insert(Point2D p, int level, KdTreeNode node) {
        if (node == null) {
            node = new KdTreeNode(p, null, null);
            return;
        }
        if (level % 2 == 0) {
            if (p.x() < node.point.x()) {
                insert(p, level + 1, node.left);
            } else {
                insert(p, level + 1, node.right);
            }
        } else {
            if (p.y() < node.point.y()) {
                insert(p, level + 1, node.left);
            } else {
                insert(p, level + 1, node.right);
            }
        }
    }

    private boolean contains(Point2D p, int level, KdTreeNode node) {
        if (node == null) {
            return false;
        }
        if (node.point.equals(p)) {
            return true;
        }
        if (level % 2 == 0) {
            if (p.x() < node.point.x()) {
                if (contains(p, level + 1, node.left)) {
                    return true;
                }
            } else {
                if (contains(p, level + 1, node.right)) {
                    return true;
                }
            }
        } else {
            if (p.y() < node.point.y()) {
                if (contains(p, level + 1, node.left)) {
                    return true;
                }
            } else {
                if (contains(p, level + 1, node.right)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void draw(int level, KdTreeNode node) {
        if (node == null) {
            return;
        }
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.point.x(), 0.0, node.point.x(), 1.0);
            draw(level + 1, node.left);
            draw(level + 1, node.right);
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(0.0, node.point.y(), 1.0, node.point.y());
            draw(level + 1, node.left);
            draw(level + 1, node.right);
        }
    }

    private void range(RectHV rect, ArrayList<Point2D> rangeList) {
    }
}

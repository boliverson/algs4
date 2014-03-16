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
        if (contains(p)) {
            return;
        }
        root = insert(root, 0, p);
        ++size;
    }

    private KdTreeNode insert(KdTreeNode node, int level, Point2D p) {
        if (node == null) {
            return new KdTreeNode(p, null, null);
        }
        if (level % 2 == 0) {
            if (p.x() < node.point.x()) {
                node.left = insert(node.left, level + 1, p);
            } else {
                node.right = insert(node.right, level + 1, p);
            }
        } else {
            if (p.y() < node.point.y()) {
                node.left = insert(node.left, level + 1, p);
            } else {
                node.right = insert(node.right, level + 1, p);
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
        StdDraw.setXscale(0.0, 1.0);
        StdDraw.setYscale(0.0, 1.0);
        draw(root, 0);
    }

    private void draw(KdTreeNode node, int level) {
        if (node == null) {
            return;
        }
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), 0.0, node.point.x(), 1.0);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(0.0, node.point.y(), 1.0, node.point.y());
        }
        draw(node.left, level + 1);
        draw(node.right, level + 1);
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> pointList = new ArrayList<Point2D>();
        range(root, 0, new RectHV(0.0, 0.0, 1.0, 1.0), rect, pointList);
        return pointList;
    }

    private void range(KdTreeNode node, int level,
            RectHV nodeRect, RectHV queryRect, ArrayList<Point2D> pointList) {
        if (node == null) {
            return;
        }
        if (!nodeRect.intersects(queryRect)) {
            return;
        }
        if (queryRect.contains(node.point)) {
            pointList.add(node.point);
        }
        if (level % 2 == 0) {
            range(node.left, level + 1,
                    new RectHV(nodeRect.xmin(),
                        nodeRect.ymin(),
                        node.point.x(),
                        nodeRect.ymax()),
                    queryRect, pointList);
            range(node.right, level + 1,
                    new RectHV(node.point.x(),
                        nodeRect.ymin(),
                        nodeRect.xmax(),
                        nodeRect.ymax()),
                    queryRect, pointList);
        } else {
            range(node.left, level + 1,
                    new RectHV(nodeRect.xmin(),
                        nodeRect.ymin(),
                        nodeRect.xmax(),
                        node.point.y()),
                    queryRect, pointList);
            range(node.right, level + 1,
                    new RectHV(nodeRect.xmin(),
                        node.point.y(),
                        nodeRect.xmax(),
                        nodeRect.ymax()),
                    queryRect, pointList);
        }
    }

    public Point2D nearest(Point2D p) {
        return nearest(root, 0, new RectHV(0.0, 0.0, 1.0, 1.0), new Point2D(root.point.x(), root.point.y()), p);
    }

    private Point2D nearest(KdTreeNode node, int level,
            RectHV nodeRect, final Point2D nearestPoint, final Point2D p) {
        if (node == null) {
            return null;
        }
        StdOut.printf("%f, %f, %f, %f\n%f, %f\n", nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax(), node.point.x(), node.point.y());

        Point2D currentNearestPoint
            = new Point2D(nearestPoint.x(), nearestPoint.y());
        if (currentNearestPoint.distanceTo(p) < nodeRect.distanceTo(p)) {
            return currentNearestPoint;
        }
        if (level % 2 == 0) {
            RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.point.x(), nodeRect.ymax());
            RectHV rightRect = new RectHV(node.point.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
            Point2D leftNearestPoint = null;
            Point2D rightNearestPoint = null;
            if (leftRect.distanceTo(p) < rightRect.distanceTo(p)) {
                if (currentNearestPoint.distanceTo(p) < leftRect.distanceTo(p)) {
                    return currentNearestPoint;
                }
                leftNearestPoint = nearest(node.left, level + 1, leftRect, currentNearestPoint, p);
                if (leftNearestPoint != null && p.distanceTo(leftNearestPoint) < p.distanceTo(currentNearestPoint)) {
                    currentNearestPoint = new Point2D(leftNearestPoint.x(), leftNearestPoint.y());
                }
                rightNearestPoint = nearest(node.right, level + 1, rightRect, currentNearestPoint, p);
                if (rightNearestPoint != null && p.distanceTo(rightNearestPoint) < p.distanceTo(currentNearestPoint)) {
                    currentNearestPoint = new Point2D(rightNearestPoint.x(), rightNearestPoint.y());
                }
            } else {
                if (currentNearestPoint.distanceTo(p) < rightRect.distanceTo(p)) {
                    return currentNearestPoint;
                }
                rightNearestPoint = nearest(node.right, level + 1, rightRect, currentNearestPoint, p);
                if (rightNearestPoint != null && p.distanceTo(rightNearestPoint) < p.distanceTo(currentNearestPoint)) {
                    currentNearestPoint = new Point2D(rightNearestPoint.x(), rightNearestPoint.y());
                }
                leftNearestPoint = nearest(node.left, level + 1, leftRect, currentNearestPoint, p);
                if (leftNearestPoint != null && p.distanceTo(leftNearestPoint) < p.distanceTo(currentNearestPoint)) {
                    currentNearestPoint = new Point2D(leftNearestPoint.x(), leftNearestPoint.y());
                }
            }
        } else {
            RectHV bottomRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.point.y());
            RectHV topRect = new RectHV(nodeRect.xmin(), node.point.y(), nodeRect.xmax(), nodeRect.ymax());
            Point2D bottomNearestPoint = null;
            Point2D topNearestPoint = null;
            if (bottomRect.distanceTo(p) < topRect.distanceTo(p)) {
                if (currentNearestPoint.distanceTo(p) < bottomRect.distanceTo(p)) {
                    return currentNearestPoint;
                }
                bottomNearestPoint = nearest(node.left, level + 1, bottomRect, currentNearestPoint, p);
                if (bottomNearestPoint != null && p.distanceTo(bottomNearestPoint) < p.distanceTo(currentNearestPoint)) {
                    currentNearestPoint = new Point2D(bottomNearestPoint.x(), bottomNearestPoint.y());
                }
                topNearestPoint = nearest(node.right, level + 1, topRect, currentNearestPoint, p);
                if (topNearestPoint != null && p.distanceTo(topNearestPoint) < p.distanceTo(currentNearestPoint)) {
                    currentNearestPoint = new Point2D(topNearestPoint.x(), topNearestPoint.y());
                }
            } else {
                if (currentNearestPoint.distanceTo(p) < topRect.distanceTo(p)) {
                    return currentNearestPoint;
                }
                topNearestPoint = nearest(node.right, level + 1, topRect, currentNearestPoint, p);
                if (topNearestPoint != null && p.distanceTo(topNearestPoint) < p.distanceTo(currentNearestPoint)) {
                    currentNearestPoint = new Point2D(topNearestPoint.x(), topNearestPoint.y());
                }
                bottomNearestPoint = nearest(node.left, level + 1, bottomRect, currentNearestPoint, p);
                if (bottomNearestPoint != null && p.distanceTo(bottomNearestPoint) < p.distanceTo(currentNearestPoint)) {
                    currentNearestPoint = new Point2D(bottomNearestPoint.x(), bottomNearestPoint.y());
                }
            }
        }
        return currentNearestPoint;
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                tree.insert(new Point2D(x / 10.0, y / 10.0));
            }
        }
        StdOut.println(tree.nearest(new Point2D(.34, .46)));
    }
}

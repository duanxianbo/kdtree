import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private double xmin;
        private double xmax;
        private double ymin;
        private double ymax;
        private final Point2D p;      // the point
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree


        public Node(Point2D p, Node lb, Node rt, double xmin, double ymin, double xmax,
                    double ymax) {
            this.p = p;
            this.lb = lb;
            this.rt = rt;
            this.xmax = xmax;
            this.xmin = xmin;
            this.ymax = ymax;
            this.ymin = ymin;
        }
    }

    public KdTree() {
        this.root = null;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return this.root == null;
    }

    public int size()                         // number of points in the set
    {
        return this.size;
    }


    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (this.contains(p)) {
            return;
        }

        this.root = this.insertRec(this.root, p, true, 0, 0, 1, 1);
        this.size++;
    }

    private Node insertRec(Node parent, Point2D p, boolean compareX, double xmin, double ymin,
                           double xmax,
                           double ymax) {
        if (parent == null) {
            return new Node(p, null, null, xmin, ymin, xmax, ymax);
        }

        parent.xmin = xmin;
        parent.xmax = xmax;
        parent.ymax = ymax;
        parent.ymin = ymin;

        if (compareX) {
            if (parent.p.x() > p.x()) {
                parent.lb = this.insertRec(parent.lb, p, false, xmin, ymin, parent.p.x(), ymax);

            }
            else {
                parent.rt = this.insertRec(parent.rt, p, false, parent.p.x(), ymin, xmax, ymax);
            }
        }
        else {
            if (parent.p.y() > p.y()) {
                parent.lb = this.insertRec(parent.lb, p, true, xmin, ymin, xmax, parent.p.y());
            }
            else {
                parent.rt = this.insertRec(parent.rt, p, true, xmin, parent.p.y(), xmax, ymax);
            }
        }

        return parent;
    }


    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return this.containsP(this.root, p, true);
    }

    private boolean containsP(Node parent, Point2D p, boolean compareX) {
        if (parent == null) {
            return false;
        }

        if (parent.p.equals(p)) {
            return true;
        }

        if (compareX) {
            if (parent.p.x() > p.x()) {
                return this.containsP(parent.lb, p, false);
            }
            else {
                return this.containsP(parent.rt, p, false);
            }
        }
        else {
            if (parent.p.y() > p.y()) {
                return this.containsP(parent.lb, p, true);
            }
            else {
                return this.containsP(parent.rt, p, true);
            }
        }
    }

    public void draw()                         // draw all points to standard draw
    {
        if (isEmpty()) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);


        RectHV rect = new RectHV(0, 0, 1, 1);

        rect.draw();

        this.drawLines(this.root, true);
    }

    private void drawLines(Node node, boolean compareX) {
        if (node == null) {
            return;
        }

        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.p.draw();
        StdDraw.setPenRadius(0.01);

        if (compareX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.ymin, node.p.x(), node.ymax);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.xmin, node.p.y(), node.xmax, node.p.y());
        }

        this.drawLines(node.lb, !compareX);
        this.drawLines(node.rt, !compareX);
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {

        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Stack<Point2D> points = new Stack<Point2D>();

        this.setRanges(this.root, rect, points, true);

        return points;
    }

    private void setRanges(Node node, RectHV rect, Stack<Point2D> points, boolean compareX) {
        if (node == null) {
            return;
        }

        if (rect.contains(node.p)) {
            points.push(node.p);
        }

        if (this.checkIntersect(node, rect, compareX)) {
            this.setRanges(node.lb, rect, points, !compareX);
            this.setRanges(node.rt, rect, points, !compareX);
        }
        else {
            if (compareX && rect.xmax() <= node.p.x() || !compareX && rect.ymax() <= node.p.y()) {
                this.setRanges(node.lb, rect, points, !compareX);
            }
            else {
                this.setRanges(node.rt, rect, points, !compareX);
            }
        }
    }


    private boolean checkIntersect(Node node, RectHV rect, boolean compareX) {
        if (rect.contains(node.p)) {
            return true;
        }

        if (!compareX) {
            return node.p.y() > rect.ymin() && node.p.y() < rect.ymax();
        }

        return node.p.x() > rect.xmin() && node.p.x() < rect.xmax();
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {

        if (p == null) {
            throw new IllegalArgumentException();
        }

        return this.getNearest(this.root, true, this.root.p, p);
    }

    private Point2D getNearest(Node node, boolean compareX, Point2D nearest,
                               Point2D p) {
        if (node == null) {
            return nearest;
        }

        double xDistanceSquared = Math.pow(Math.abs(p.x() - node.p.x()), 2);
        double yDistanceSquared = Math.pow(Math.abs(p.y() - node.p.y()), 2);
        double nearestDistance = nearest.distanceSquaredTo(p);
        double coorDistanceSquared = compareX ? xDistanceSquared : yDistanceSquared;

        double newDistanceSquared = node.p.distanceSquaredTo(p);
        Point2D newNearest = newDistanceSquared < nearestDistance ? node.p : nearest;


        if (compareX && (node.p.x() >= p.x()) || !compareX && (node.p.y() >= p.y())) {
            Point2D lbNearest = getNearest(node.lb, !compareX, newNearest, p);

            if (lbNearest.distanceSquaredTo(p) <= coorDistanceSquared) {
                return lbNearest;
            }

            return getNearest(node.rt, !compareX, lbNearest, p);
        }
        else {
            Point2D rtNearest = getNearest(node.rt, !compareX, newNearest, p);

            if (rtNearest.distanceSquaredTo(p) <= coorDistanceSquared) {
                return rtNearest;
            }

            return getNearest(node.lb, !compareX, rtNearest, p);
        }
    }

    private static boolean isContains(double[] coords, double current) {
        for (double d : coords) {
            if (d == current) {
                return true;
            }
        }

        return false;
    }


    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        KdTree kd = new KdTree();
        int n = Integer.parseInt(args[0]);
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            kd.insert(new Point2D(x, y));
            StdOut.printf("%8.6f %8.6f\n", x, y);
        }


        int index = 0;
        double[] coords = new double[4];

        while (index != 4) {
            double current = StdRandom.uniform(0.0, 1.0);
            if (!isContains(coords, current)) {
                coords[index] = current;
                index++;
            }
        }

        double xmin = Math.min(coords[0], coords[2]);
        double xmax = Math.max(coords[0], coords[2]);
        double ymin = Math.min(coords[1], coords[3]);
        double ymax = Math.max(coords[1], coords[3]);

        RectHV queryRect = new RectHV(xmin, ymin, xmax, ymax);


        kd.draw();

        StdDraw.setPenColor(StdDraw.RED);
        queryRect.draw();
        Iterable<Point2D> points = kd.range(queryRect);
        StdOut.printf("all points in ranges:\n");
        for (Point2D point : points) {
            StdOut.printf("%8.6f %8.6f\n", point.x(), point.y());

            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.setPenRadius(0.03);
            point.draw();
        }

        double queryX = StdRandom.uniform(0.0, 1.0);
        double queryY = StdRandom.uniform(0.0, 1.0);


        Point2D queryP = new Point2D(queryX, queryY);

        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(0.02);
        queryP.draw();
        Point2D nearest = kd.nearest(queryP);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(0.01);
        StdDraw.line(queryX, queryY, nearest.x(), nearest.y());
    }
}

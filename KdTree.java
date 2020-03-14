import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class KdTree {
    private Node root;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree


        public Node(Point2D p, Node lb, Node rt, RectHV rect) {
            this.p = p;
            this.lb = lb;
            this.rt = rt;
            this.rect = rect;
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
        return this.getSubTreeNumber(this.root, 0);
    }

    private int getSubTreeNumber(Node parent, int number) {
        if (parent == null) {
            return 0;
        }

        number++;
        return this.getSubTreeNumber(parent.lb, number) + this.getSubTreeNumber(parent.rt, number);
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (this.contains(p)) {
            return;
        }

        this.root = this.insertRec(this.root, p, true, new RectHV(0, 0, 1, 1));
    }

    private Node insertRec(Node parent, Point2D p, boolean compareX, RectHV rect) {
        if (parent == null) {
            return new Node(p, null, null, rect);
        }

        parent.rect = rect;

        if (compareX) {
            if (parent.p.x() > p.x()) {
                RectHV r = new RectHV(rect.xmin(), rect.ymin(), parent.p.x(), rect.ymax());
                parent.lb = this.insertRec(parent.lb, p, false, r);

            }
            else {
                RectHV r = new RectHV(parent.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                parent.rt = this.insertRec(parent.rt, p, false, r);
            }
        }
        else {
            if (parent.p.y() > p.y()) {
                RectHV r = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), parent.p.y());
                parent.lb = this.insertRec(parent.lb, p, true, r);
            }
            else {
                RectHV r = new RectHV(rect.xmin(), parent.p.y(), rect.xmax(), rect.ymax());
                parent.rt = this.insertRec(parent.rt, p, true, r);
            }
        }

        return parent;
    }


    public boolean contains(Point2D p)            // does the set contain point p?
    {
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
        this.root.rect.draw();

        this.drawLines(this.root, true);
    }

    private void drawLines(Node node, boolean compareX) {
        if (node == null) {
            return;
        }

        if (compareX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }

        this.drawLines(node.lb, !compareX);
        this.drawLines(node.rt, !compareX);
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        Stack<Point2D> ranges = new Stack<Point2D>();

        return ranges;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        return null;
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

        kd.draw();

    }
}

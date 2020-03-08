import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> bstTree;
    private int test;


    public PointSET()                               // construct an empty set of points
    {
        this.bstTree = new TreeSet<Point2D>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return this.bstTree.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return this.bstTree.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        this.bstTree.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        return bstTree.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {

    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {

    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (this.isEmpty()) {
            return null;
        }
        double distance = -1;
        Point2D current = this.bstTree.;

        while (distance == -1 && current != null) {
            distance = this.getDistance();
        }

        return null;

    }

    private double getDistance(Point2D p1, Point2D p2) {
        return p1.distanceTo(p2);
    }

    public static void main(String[] args) {

    }
}

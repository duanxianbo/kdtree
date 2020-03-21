import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> bstTree;


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
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.bstTree.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return bstTree.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D point : this.bstTree) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Stack<Point2D> point2DS = new Stack<Point2D>();

        if (this.isEmpty()) {
            return point2DS;
        }

        for (Point2D point : this.bstTree) {
            if (rect.contains(point)) {
                point2DS.push(point);
            }
        }

        return point2DS;

    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.isEmpty()) {
            return null;
        }
        Point2D nearestP = null;
        double distance = -1;


        for (Point2D point : this.bstTree) {
            double currentDistance = this.getDistance(p, point);

            if (nearestP == null || currentDistance < distance) {
                distance = currentDistance;
                nearestP = point;
            }

        }

        return nearestP;

    }


    private double getDistance(Point2D p1, Point2D p2) {
        return p1.distanceSquaredTo(p2);
    }

}

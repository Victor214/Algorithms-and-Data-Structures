import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private final SET<Point2D> RedBlack;

    // construct an empty set of points
    public PointSET() {
        RedBlack = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return RedBlack.isEmpty();
    }

    // number of points in the set
    public int size() {
        return RedBlack.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (contains(p)) // Don't add repeated points.
            return;
        RedBlack.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return RedBlack.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        for (Point2D p : RedBlack) {
            p.draw();
        }
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> result = new ArrayList<Point2D>();
        for (Point2D p : RedBlack) {
            if (rect.contains(p))
                result.add(p);
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty())
            return null;

        Point2D champion = RedBlack.iterator().next(); // Get first element? Does this work?
        for (Point2D v : RedBlack) {
            if (v.distanceTo(p) < champion.distanceTo(p))
                champion = v;
        }
        return champion;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}

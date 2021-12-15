import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private final static boolean VERTICAL = true;
    private final static boolean HORIZONTAL = false;
    private Node root;

    private class Node {
        public int count;
        private boolean direction;
        private Point2D p;
        private Node left, right;
        private RectHV rectangle; // The rectangle for a given node. Easier to calculate on insertion and keep it.

        private RectHV calculateRectangle(Node parent, boolean direction, Boolean isLeft) {
            if (parent == null) // We are adding the root
                return new RectHV(0, 0, 1, 1);
            RectHV parentRect = parent.rectangle;
            if (direction == VERTICAL) { // x compare
                if (isLeft) { // Left side of parent
                    return new RectHV(parentRect.xmin(), parentRect.ymin(), parent.p.x(), parentRect.ymax()); // Same values of previous rectangle, except we use the parent point's x to spli it.
                } else {
                    return new RectHV(parent.p.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
                }
            } else {
                if (isLeft) { // Upper side of parent
                    return new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), parent.p.y()); // Same values of previous rectangle, except we use the parent point's x to spli it.
                } else { // Bottom side of parent
                    return new RectHV(parentRect.xmin(), parent.p.y(), parentRect.xmax(), parentRect.ymax());
                }
            }
        }

        public Node(Node parent, Point2D p, boolean direction, Boolean isLeft) {
            this.rectangle = calculateRectangle(parent, !direction, isLeft); // To calculate the rectangle exclusively, we use the parent's direction instead.
            this.p = p;
            this.count = 1;
            this.direction = direction;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        return x.count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = put(null, root, p, true, null); // isLeft parameter here is irrelevant, considering we will be creating the default rectangle, as it is the root.
    }

    private int verticalCompare(Point2D insertPoint, Point2D nodePoint) {
        if (insertPoint.x() > nodePoint.x())
            return 1;
        else
            return -1;
    }

    private int horizontalCompare(Point2D insertPoint, Point2D nodePoint) {
        if (insertPoint.y() > nodePoint.y())
            return 1;
        else
            return -1;
    }

    // isVerticalLine checks if its a vertical compare to be done, which means using x and evaluating if reference point falls left or right to x.
    // if isVerticalLine is false, then it is an horizontal compare, which means using y and evaluating if reference point falls below (left) or above (right) of y.
    private Node put(Node parent, Node x, Point2D p, boolean direction, Boolean isLeft) {
        if (x == null) // Null means we got to a null link, so add it.
            return new Node(parent, p, direction, isLeft);
        if (p.equals(x.p)) // We want no repeated values, so break here and do nothing.
            return x;
        int compare = direction ? verticalCompare(p, x.p) : horizontalCompare(p, x.p);
        if (compare > 0)
            x.right = put(x, x.right, p, !direction, false);
        else
            x.left = put(x, x.left, p, !direction, true);
        x.count = 1 + size(x.left) + size(x.right); // Keeps track of its size. A given tree's size is itself (-1), plus the size of each child tree.
        return x;
    }

    private Point2D get(Point2D p) {
        Node x = root;
        boolean direction = true;
        while (x != null) {
            if (p.equals(x.p)) // Found the point
                return p;
            int compare = direction ? verticalCompare(p, x.p) : horizontalCompare(p, x.p);
            if (compare > 0)
                x = x.right;
            else
                x = x.left;
            direction = !direction;
        }
        return null;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return get(p) != null;
    }


    // draw all points to standard draw
    public void draw() {
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        draw(root);
        StdDraw.show();
    }

    // Draw recursively, from root to leaf nodes.
    private void draw(Node x) {
        if (x == null)
            return;
        x.p.draw();
        if (x.direction == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rectangle.ymin(), x.p.x(), x.rectangle.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rectangle.xmin(), x.p.y(), x.rectangle.xmax(), x.p.y());
        }
        /*
        StdDraw.setPenColor(StdDraw.GREEN);
        x.rectangle.draw();
        */

        draw(x.left);
        draw(x.right);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> pointsInRange = new ArrayList<>();
        range(root, rect, pointsInRange);
        return pointsInRange;
    }

    private void range(Node x, RectHV queryRect, List<Point2D> pointsInRange) {
        if (x == null)
            return;
        /*
        if (!queryRect.intersects(x.rectangle)) // If it doesn't intersect the rectangle that represents current point, then return, nothing to be found here.
            return;
         */
        if (queryRect.contains(x.p)) // If current point is within, add it to the list
            pointsInRange.add(x.p);
        range(x.left, queryRect, pointsInRange);
        range(x.right, queryRect, pointsInRange);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty())
            return null;
        Node x = nearest(root, root, p);
        return x.p;
    }

    private Node nearest(Node x, Node champ, Point2D qp) {
        if (x == null)
            return champ;

        double queryToRecDistance = x.rectangle.distanceTo(qp);
        double queryToChampDistance = champ.p.distanceTo(qp);
        if (queryToRecDistance > queryToChampDistance) // If the query point to current rectangle (shortest) distance is greater than the query point to current champion distance, stop here.
            return champ; // Practically, this means the champion cannot be within the current rectangle, considering the current champion is already better than anything inside the rectangle.
        double queryToNodeDistance = x.p.distanceTo(qp); // Distance between query point and current node's point.
        if (queryToNodeDistance < queryToChampDistance) // Current node is the new champion
            champ = x;

        // Proceed to next step, but in rectangle closest to qp.
        double queryToLeftRecDistance = x.left != null ? (x.left.rectangle.distanceTo(qp)) : 0;
        double queryToRightRecDistance = x.right != null ? (x.right.rectangle.distanceTo(qp)) : 0;
        if (queryToLeftRecDistance < queryToRightRecDistance) {
            champ = nearest(x.left, champ, qp);
            champ = nearest(x.right, champ, qp);
        } else {
            champ = nearest(x.right, champ, qp);
            champ = nearest(x.left, champ, qp);
        }

        return champ;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}

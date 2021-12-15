import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private final List<LineSegment> segmentsAL;
    private int segmentCount;

    // Array of sorted slopes relative to point "point", with equal slopes ranging from v to w in array, both inclusive.
    private void attemptSaveSegment(Point[] pointsSlopeOrder, Point point, int v, int w) {
        // Now sort each group by natural order, and only add as a line segment if the current point is the peak (as in P in P->A->B->C)
        Arrays.sort(pointsSlopeOrder, v, w + 1); // +1 as the toIndex range is exclusive.
        if (point.compareTo(pointsSlopeOrder[v]) < 0) { // If the reference point "point" is less than the least of all natural-order-sorted points, it means its the first one.
            segmentsAL.add(new LineSegment(point, pointsSlopeOrder[w])); // Add the point from "point" to "point[w]", as that is the maximum size.
            segmentCount++;
        }
    }

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        segmentsAL = new ArrayList<>();

        if (points.length < 4) {
            segments = segmentsAL.toArray(new LineSegment[]{});
            return;
        }

        for (int i = 0; i < points.length; i++) {
            /*
            Point[] pointsSlopeOrder;
            List<Point> pointsAL = new ArrayList<>(Arrays.asList(points));
            pointsAL.remove(i); // Remove current point (Otherwise, can also remove based on the object).
            pointsSlopeOrder = (Point[]) pointsAL.toArray();
             */

            // Get all points except points[i] into the array, and sort it by slope order, which will group colinear points.
            Point[] pointsSlopeOrder = new Point[points.length - 1];
            int arSize = 0;
            for (int j = 0; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    continue; // In case it is the same point, keep going.
                pointsSlopeOrder[arSize++] = points[j];
            }
            Arrays.sort(pointsSlopeOrder, points[i].slopeOrder());

            // Debug
            /*
            System.out.println("-------------");
            System.out.println("Reference Point : " + points[i].toString());
            System.out.print("Points / Slope : ");
            for (int j = 0; j < points.length - 1; j++)
                System.out.print(" | [" + pointsSlopeOrder[j].toString() + " + " + points[i].slopeTo(pointsSlopeOrder[j]) + "]");
            System.out.println();
            System.out.println("-------------");
            */

            // Loop thru the sorted array, detecting groups of at least 4, and analyzing each group in-depth (for natural order and so forth).
            double currentSlope = points[i].slopeTo(pointsSlopeOrder[0]); // The streak's slope
            int slopeStreak = 0;
            for (int j = 0; j < pointsSlopeOrder.length; j++) {
                double iterationSlope = points[i].slopeTo(pointsSlopeOrder[j]); // the current iteration's slope
                if (currentSlope != iterationSlope) {
                    if (slopeStreak >= 3) { // Current streak has finally ended, and it is at least 3 in size (3 consecutive slopes mean its 4 points, relative to point i).
                        attemptSaveSegment(pointsSlopeOrder, points[i], j - slopeStreak, j - 1);
                    }
                    currentSlope = iterationSlope;
                    slopeStreak = 1;
                    continue;
                }
                slopeStreak++;
            }

            // Array has ended, and there's a case where the streak was met, but it hasn't been broken for the segment to be added, so check it here finally.
            if (slopeStreak >= 3) {
                attemptSaveSegment(pointsSlopeOrder, points[i], pointsSlopeOrder.length - slopeStreak, pointsSlopeOrder.length - 1);
            }
        }

        // Finally, convert the output
        segments = segmentsAL.toArray(new LineSegment[]{});
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentCount;
    }

    // the line segments
    public LineSegment[] segments() {
        return this.segments;
    }


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

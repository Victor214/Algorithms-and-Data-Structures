import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int count;

    private void formatOutput(List<LineSegment> segmentsAL) {
        segments = segmentsAL.toArray(new LineSegment[]{});
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        // No null arrays as input
        if (points == null)
            throw new IllegalArgumentException();

        // No null or duplicate points
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();

            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == points[j])
                    throw new IllegalArgumentException();
            }
        }

        List<LineSegment> segmentsAL = new ArrayList<>();
        this.count = 0;

        // No arrays with less than 4 points
        if (points.length < 4) {
            formatOutput(segmentsAL);
            return;
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        double slope1 = points[i].slopeTo(points[j]);
                        double slope2 = points[i].slopeTo(points[k]);
                        double slope3 = points[i].slopeTo(points[l]);
                        if (!(slope1 == slope2 && slope2 == slope3)) // Points are not in the same line segment.
                            continue;

                        // Debug
                        /*
                        System.out.println("-------------------");
                        System.out.println("Reference Point : " + points[i].toString());
                        System.out.println("Slopes : " + slope1 + " / " + slope2 + " / " + slope3);
                        System.out.println("Points : " + points[j].toString() + " | " + points[k].toString() + " | " + points[l].toString());
                        System.out.println("-------------------");
                         */
                        
                        // Draw from min to max to make sure it draws correctly.
                        int min = i;
                        if (points[j].compareTo(points[min]) < 0) min = j;
                        if (points[k].compareTo(points[min]) < 0) min = k;
                        if (points[l].compareTo(points[min]) < 0) min = l;

                        int max = i;
                        if (points[j].compareTo(points[max]) > 0) max = j;
                        if (points[k].compareTo(points[max]) > 0) max = k;
                        if (points[l].compareTo(points[max]) > 0) max = l;

                        LineSegment thisSegment = new LineSegment(points[min], points[max]);
                        segmentsAL.add(thisSegment);
                        count++;
                    }
                }
            }
        }

        formatOutput(segmentsAL);
    }

    // the number of line segments
    public int numberOfSegments() {
        return count;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            // segment.draw();
        }
        StdDraw.show();
    }
}

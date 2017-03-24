import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Project: Sonar-Gerrit Plugin
 * Author:  Tatiana Didik
 * Created: 24.02.2016 18:50
 * <p/>
 * $Id$
 */
public class FastCollinearPoints1 {
    // finds all line segments containing 4 or more points

    private Point[][] segments;

    public FastCollinearPoints1(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        this.segments = new Point[0][2];


        for (int i = 0; i < points.length; i++) {
            Point[] pointsCopy = new Point[points.length];    // make faster: sort by coords first
            System.arraycopy(points, 0, pointsCopy, 0, points.length);
            if (i < points.length - 1) {
                Arrays.sort(pointsCopy, i + 1, points.length, points[i].slopeOrder());
            }
            if (i > 0) {
                Arrays.sort(pointsCopy, 0, i, points[i].slopeOrder());
            }
            int j = 0;
            while (j < pointsCopy.length) {
                // check if array contains duplicate point
                if (j != i && points[i].compareTo(pointsCopy[j]) == 0) {
                    throw new IllegalArgumentException("Duplicate point");
                }

                // check if we got to i-item in the copy
                if (j == i) {
                    j++;
                    continue;
                }

                // check if we've processed this line already
                boolean alreadyProcessed = false;
                int g = 0;
                while (g < segments.length && !alreadyProcessed) {
                    if (pointsCopy[j] == segments[g][0] || pointsCopy[j] == segments[g][1] || isOnLine(pointsCopy[j], segments[g][0], segments[g][1])) {
                        alreadyProcessed = true;
                    }
                    g++;
                }
                if (alreadyProcessed) {
                    j++;
                    continue;
                }

                // processing
                double slope = pointsCopy[i].slopeTo(pointsCopy[j]);
                int k = 0;
                while (j + 1 + k < pointsCopy.length && (j + 1 + k == i || slope == points[i].slopeTo(pointsCopy[j + 1 + k]))) {
                    k++;
                }
                boolean passedI = k > 0 && j < i && j + 1 + k >= i;

                int count = passedI ? k - 1 : k;
                if (count >= 2) { // points[i], pointsCopy[j] + 2 -> 4 points in line
                    Point[] pointsOnLine = new Point[count + 2];
                    if (passedI) {
                        System.arraycopy(pointsCopy, j, pointsOnLine, 0, i - j);
                        System.arraycopy(pointsCopy, i + 1, pointsOnLine, i - j, j + k - i);
                    } else {
                        System.arraycopy(pointsCopy, j, pointsOnLine, 0, k + 1);
                    }
                    pointsOnLine[count + 1] = points[i];
                    Point[] lineBorders = getEdge(pointsOnLine);
                    resize();
                    segments[numberOfSegments() - 1] = lineBorders;
                }
                j += k + 1;
            }
        }
    }

    private Point[] getEdge(Point... points) {
        Point min = points[0];
        Point max = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(min) < 0) {
                min = points[i];
            } else if (points[i].compareTo(max) > 0) {
                max = points[i];
            }
        }
        return new Point[]{min, max};
    }

    private boolean isOnLine(Point a, Point b, Point c) {
        return a.slopeTo(b) == a.slopeTo(c) || a.slopeTo(b) == 180 - a.slopeTo(c) || a.slopeTo(b) == 180 + a.slopeTo(c);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[segments.length];
        for (int i = 0; i < segments.length; i++) {
            Point[] segment = segments[i];
            res[i] = new LineSegment(segment[0], segment[1]);
        }
        return res;
    }

    private void resize() {
        Point[][] newArr = new Point[numberOfSegments() + 1][2];
        int j = -1;
        if (newArr.length > 1) {
            for (int i = 0; i < numberOfSegments(); i++) {
                newArr[++j] = segments[i];
            }
        }
        segments = newArr;
    }


    public static void main(String[] args) {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints1 collinear = new FastCollinearPoints1(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}

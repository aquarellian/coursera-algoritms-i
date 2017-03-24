import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project: Sonar-Gerrit Plugin
 * Author:  Tatiana Didik
 * Created: 02.03.2016 12:06
 * <p/>
 * $Id$
 */
public class FastCollinearPoints {

    // finds all line segments containing 4 or more points

    //    private Point[][] segments;
    private LineSegment[] finalSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        List<Point[]> segments = new ArrayList<>();

        for (int i = 0; i < points.length - 1; i++) {
            Point point1 = points[i];
            PointInfo[] pointsCopy = new PointInfo[points.length];
            for (int j = 0; j < points.length; j++)






















































































































































































































































































































































































































































































































































                0p-
            System.arraycopy(points, 0, pointsCopy, 0, points.length);
            Arrays.sort(pointsCopy, 0, pointsCopy.length, point1.slopeOrder());


            int j = 1;

            // if there will be duplicate, after sorting initial value will have index 0 (because slope to itself is -Infinity)
            // and duplicate - index 1 (because slope to its original will be -Infinity, too)
            // we don't care if there are more than 2 duplicates.
            if (pointsCopy[0].compareTo(pointsCopy[1]) == 0 && pointsCopy[0] != pointsCopy[j]) {
                throw new IllegalArgumentException("Duplicate point");
            }

            Double nextSlope = null;
            while (j + 2 < pointsCopy.length) {

                double slope1 = nextSlope == null? point1.slopeTo(pointsCopy[j]) : nextSlope;
                double slope2 = point1.slopeTo(pointsCopy[j + 1]);
                double slope3 = point1.slopeTo(pointsCopy[j + 2]);
                if (isOnLine(slope1, slope2) && isOnLine(slope1, slope3)) {
                    int start = j;
                    int end = j + 2;
                    nextSlope = point1.slopeTo(pointsCopy[end + 1]);
                    while (end + 1 < pointsCopy.length && isOnLine(slope1, nextSlope)) {
                        end++;
                        nextSlope = point1.slopeTo(pointsCopy[end + 1]);
                    }
                    int count = end - start + 2;
                    Point[] pointsOnLine = new Point[count];
                    System.arraycopy(pointsCopy, start, pointsOnLine, 0, count - 1);
                    pointsOnLine[pointsOnLine.length - 1] = point1;
                    Point[] lineBorders = getEdge(pointsOnLine);
                    if (!findSegment(lineBorders, segments)) {
                        segments.add(lineBorders);
                    }
                    j = end + 1;
                } else {
                    j++;
                }
            }

        }
//            Double slope = null;
//            int start = 0;
//            int end = 0;
//            int j = 0;
//            do {
//                Point point2 = pointsCopy[j];
//                if (point1.compareTo(point2) == 0 && point1 != point2) {
//                    throw new IllegalArgumentException("Duplicate point");
//                }
//                Double newSlope = point1 == point2 ? null : point1.slopeTo(point2);
//                if (slope == null || !isOnLine(slope, newSlope)) {
//                    // process found line of points
//                    if (slope != null && newSlope != null) {
//                        int count = end - start + 2;
//                        if (count >= 4) {
//                            Point[] pointsOnLine = new Point[count];
//                            System.arraycopy(pointsCopy, start, pointsOnLine, 0, count - 1);
//                            pointsOnLine[pointsOnLine.length - 1] = point1;
//                            Point[] lineBorders = getEdge(pointsOnLine);
//                            if (!findSegment(lineBorders, segments)) {
//                                segments.add(lineBorders);
//                            }
//                        }
//                    }
//                    //reset search
//                    slope = newSlope;
//                    start = j;
//                    end = j;
//                } else {
//                    end++;
//                }
//                j++;
//            } while (j < pointsCopy.length && start <= end && end < pointsCopy.length);
//            int count = end - start + 2;
//            if (count >= 4) {
//                Point[] pointsOnLine = new Point[count];
//                System.arraycopy(pointsCopy, start, pointsOnLine, 0, count - 1);
//                pointsOnLine[pointsOnLine.length - 1] = point1;
//                Point[] lineBorders = getEdge(pointsOnLine);
//                if (!findSegment(lineBorders, segments)) {
//                    segments.add(lineBorders);
//                }
//            }
//        }
        finalSegments = new LineSegment[segments.size()];
        for (int i = 0; i < segments.size(); i++) {
            Point[] point = segments.get(i);
            finalSegments[i] = new LineSegment(point[0], point[1]);
        }
    }

    private boolean findSegment(Point[] borders, List<Point[]> segments) {
        assert borders.length == 2;
        for (int i = 0; i < segments.size(); i++) {
            Point[] segment = segments.get(i);
            if (segment[0] == borders[0] && segment[1] == borders[1]) {
                return true;
            }
        }
        return false;
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

//    private boolean isOnLine(Point a, Point b, Point c) {
//        return a.slopeTo(b) == a.slopeTo(c) || a.slopeTo(b) == 180 - a.slopeTo(c) || a.slopeTo(b) == 180 + a.slopeTo(c);
//    }

    private boolean isOnLine(Double slope1, Double slope2) {
        return slope1 != null && slope2 != null && (doubleEquals(slope1, slope2));
//                || doubleEquals(slope1, Math.PI - slope2)
//                || doubleEquals(slope1, Math.PI + slope2));
    }

    private boolean doubleEquals(double d1, double d2) {
        return d1 == d2 || Math.abs(d1 - d2) < 0.00000001;
    }

    // the number of line segments
    public int numberOfSegments() {
        return finalSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[finalSegments.length];
        System.arraycopy(finalSegments, 0, res, 0, finalSegments.length);
        return res;
    }

    public static void main(String[] args) {

//        Point[] points = new Point[]{new Point(28851, 24382), new Point(25429, 28332), new Point(28851, 24382)};
//        // draw the points
//        StdDraw.show(0);
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();
//        FastCollinearPoints collinear = new FastCollinearPoints(points);
//        for (LineSegment segment : collinear.segments()) {
//            StdOut.println(segment);
//            segment.draw();
//        }


        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
//         points = new Point[N];
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

//        FastCollinearPoints collinear = new FastCollinearPoints(points);

//        LineSegment[] segments = collinear.segments();
//        LineSegment[] copy = new LineSegment[segments.length];
//        for (int i = 0; i < segments.length; i++) {
//            LineSegment segment = segments[i];
//            copy[i] = segments[i];
//        }
//        System.out.println("Original");
//        System.out.println(collinear.numberOfSegments());
//        printArray(segments);
//
//        shuffle(segments);
//        System.out.println("Shuffled");
//        printArray(segments);
//
//        System.out.println("Restored");
//        segments = collinear.segments();
//        System.out.println(collinear.numberOfSegments());
//        printArray(segments);
//
//        for (int i = 0; i < copy.length; i++) {
//
//            System.out.println(copy[i] == segments[i]);
//        }

    }

    private static void printArray(Object[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    private static void shuffle(Object[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int index = StdRandom.uniform(i + 1);
            exch(arr, i, index);
        }
    }

    private static void exch(Object[] a, int i, int j) {
        Object v = a[i];
        a[i] = a[j];
        a[j] = v;
    }

    private static class PointInfo implements Comparable<PointInfo>{
        Point point;
        double slope;
        int position;

        public PointInfo(Point point, double slope, int position) {
            this.point = point;
            this.slope = slope;
            this.position = position;
        }

        @Override
        public int compareTo(PointInfo o) {
            double v = slope - o.slope;
            return v > 0.0 ? 1 : v < 0 ? -1 : 0;
        }
    }

}


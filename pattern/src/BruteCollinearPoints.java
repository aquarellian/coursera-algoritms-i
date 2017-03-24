import java.util.Arrays;

/**
 * Project: Sonar-Gerrit Plugin
 * Author:  Tatiana Didik
 * Created: 24.02.2016 18:28
 * <p/>
 * $Id$
 */
public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        //todo find duplicates if ()
        this.segments = new LineSegment[0];

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0){
                     throw new IllegalArgumentException(); // should not be equal points
                }
                for (int k = j + 1; k < points.length; k++) {
                    if (isOnLine(points[i], points[j], points[k])) {
                        Point[] lineBorders = getEdge(points[i], points[j], points[k]);
                        boolean got4points = false;
                        for (int l = k + 1; l < points.length; l++) {
                            if (isOnLine(lineBorders[0], lineBorders[1], points[l])) {
                                lineBorders = getEdge(lineBorders[0], lineBorders[1], points[l]);
                                got4points = true;
                            }
                        }
                        if (got4points){
                            resize();
                            segments[numberOfSegments() - 1] = new LineSegment(lineBorders[0], lineBorders[1]);
                        }
                    }
                }
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
        System.arraycopy(segments, 0, res, 0, segments.length);
        return res;
    }

    private void resize() {
        LineSegment[] newArr = new LineSegment[numberOfSegments() + 1];
        int j = -1;
        if (newArr.length > 1) {
            for (int i = 0; i < numberOfSegments(); i++) {
                newArr[++j] = segments[i];
            }
        }
        segments = newArr;
    }
}

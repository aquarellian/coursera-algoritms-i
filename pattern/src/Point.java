import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

/**
 * Project: Pattern Recognition
 * Author:  Tatiana Didik
 * Created: 24.02.2016 18:06
 * <p/>
 * $Id$
 */
public class Point implements Comparable<Point> {
    private int x;
    private int y;

    // constructs the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // string representation
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        return this.y == that.y ? this.x - that.x : this.y - that.y;
    }

    // the slope between this point and that point
    public double slopeTo(Point that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;
        if (dy == 0) {
            if (dx == 0) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return +0;
            }
        } else if (dx == 0) {
            return Double.POSITIVE_INFINITY;
        } else {
            return dy / dx;
        }
    }

    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double v = Point.this.slopeTo(o1) - Point.this.slopeTo(o2);
                return v > 0.0 ? 1 : v < 0 ? -1 : 0;
            }
        };
    }
}
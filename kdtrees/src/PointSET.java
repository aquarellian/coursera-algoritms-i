import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Set;
import java.util.TreeSet;

/**
 * Project: Sonar-Gerrit Plugin
 * Author:  Tatiana Didik
 * Created: 15.03.2016 14:23
 * <p/>
 * $Id$
 */
public class PointSET {

    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2D : set) {
            StdDraw.point(point2D.x(), point2D.y());
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Set<Point2D> points = new TreeSet<Point2D>();
        for (Point2D point2D : set) {
            if (point2D.x() >= rect.xmin() && point2D.x() <= rect.xmax() &&
                    point2D.y() >= rect.ymin() && point2D.y() <= rect.ymax()) {
                points.add(point2D);
            }
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
//        if (contains(p)) {
//            return p;
//        }
        double distanceSQ = 2.1; // (0,0) <= x,y <= (1,1) , distance is < that sqrt(2)  ,  distance^2 <=2.
        Point2D point = null;
        for (Point2D point2D : set) {
//            if (!p.equals(point2D)) {
            double dist = p.distanceSquaredTo(point2D);
            if (dist < distanceSQ) {
                point = point2D;
                distanceSQ = dist;
            }
//            }
        }
        return point;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {    //         create initial board from file
//        In in = new In("./kdtree/circle4.txt");
//        int N = in.readInt();
//        int[][] blocks = new int[N][N];
//        for (int i = 0; i < N; i++)
//            for (int j = 0; j < N; j++)
//                blocks[i][j] = in.readInt();
//        PointSET initial = new PointSET(blocks);


    }
}

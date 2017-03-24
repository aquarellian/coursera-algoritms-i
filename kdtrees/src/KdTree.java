import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Project: Sonar-Gerrit Plugin
 * Author:  Tatiana Didik
 * Created: 15.03.2016 14:23
 * <p/>
 * $Id$
 */
public class KdTree {

    private Node tree;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return tree == null;
    }

    // number of points in the set
    public int size() {
        return tree == null ? 0 : tree.size;//size(tree);
    }

//    private int size(Node tree) {
//        if (tree == null) {
//            return 0;
//        } else {
//            return 1 + size(tree.right) + size(tree.left);
//        }
//    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        tree = insert(p, tree, 0);
    }

    private Node insert(Point2D p, Node subtree, int deep) {
        if (subtree == null) {
            subtree = new Node(p);
            subtree.size = 1;
        } else {
            if (goLeft(p, subtree, deep)) {
                Node n = insert(p, subtree.left, deep + 1);
                subtree.left = n;
                subtree.size = 1 + size(subtree.left) + size(subtree.right);
            } else if (!p.equals(subtree.value)) {
                Node n = insert(p, subtree.right, deep + 1);
                subtree.right = n;
                subtree.size = 1 + size(subtree.left) + size(subtree.right);
            }
        }
        return subtree;
    }

    private int size(Node subtree) {
        return subtree == null ? 0 : subtree.size;
    }

    private boolean goLeft(Point2D p, Node subtree, int deep) {
        return deep % 2 == 0 ? p.x() < subtree.value.x() : p.y() < subtree.value.y();
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(p, tree, 0);
    }

    private boolean contains(Point2D p, Node subtree, int deep) {
        if (subtree == null) {
            return false;
        } else {
            if (p.equals(subtree.value)) {
                return true;
            } else if (goLeft(p, subtree, deep)) {
                return contains(p, subtree.left, deep + 1);
            } else/* if (p.x() > subtree.value.x())*/ {
                return contains(p, subtree.right, deep + 1);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(tree);
    }

    private void draw(Node subtree) {
        if (subtree != null) {
            StdDraw.point(subtree.value.x(), subtree.value.y());
            draw(subtree.left);
            draw(subtree.right);
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return range(rect, tree, 0, new TreeSet<Point2D>(), new RectHV(0d, 0d, 1d, 1d));
    }

//    private Collection<Point2D> range(RectHV rect, Node subtree, int deep, Collection<Point2D> found) {
//        if (subtree == null) {
//            return found;
//        }
//        if (rect.contains(subtree.value)) {
//            found.add(subtree.value);
//        }
//        Point2D lb = new Point2D(rect.xmin(), rect.ymin());
//        Point2D rt = new Point2D(rect.xmax(), rect.ymax());
//
//        boolean lbIsOnLeft = goLeft(lb, subtree, deep);
//        boolean rtIsOnRight = goLeft(rt, subtree, deep);
//        boolean rectIsOnLeft = lbIsOnLeft && rtIsOnRight;
//        boolean rectIsOnRight = !(lbIsOnLeft || rtIsOnRight);
//        boolean rectIsAround = lbIsOnLeft && !rtIsOnRight;
//
//        if (rectIsOnLeft) {     // todo simplify
//            found.addAll(range(rect, subtree.left, deep + 1, found));
//        } else if (rectIsOnRight) {
//            found.addAll(range(rect, subtree.right, deep + 1, found));
//        } else if (rectIsAround) {
//            found.addAll(range(rect, subtree.left, deep + 1, found));
//            found.addAll(range(rect, subtree.right, deep + 1, found));
//        }
//
//        // }
//        return found;
//
//    }

    private Collection<Point2D> range(RectHV rect, Node subtree, int deep, Collection<Point2D> found, RectHV parentRect) {
        if (subtree == null) {
            return found;
        }
        if (rect.contains(subtree.value)) {
            found.add(subtree.value);
        }
        RectHV leftRect = deep % 2 == 0 ?
                new RectHV(parentRect.xmin(), parentRect.ymin(), subtree.value.x(), parentRect.ymax()) :
                new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), subtree.value.y());
        RectHV rightRect = deep % 2 == 0 ?
                new RectHV(subtree.value.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax()) :
                new RectHV(parentRect.xmin(), subtree.value.y(), parentRect.xmax(), parentRect.ymax());

        boolean left = leftRect.intersects(rect);
        boolean right = rightRect.intersects(rect);
        if (left) {
            found.addAll(range(rect, subtree.left, deep + 1, found, leftRect));
        }
        if (right) {
            found.addAll(range(rect, subtree.right, deep + 1, found, rightRect));
        }
        return found;

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // (0,0) <= x,y <= (1,1) , distance is < that sqrt(2)  ,  distance^2 <=2.
        Neighbor closestNeighbor = new Neighbor(null, 2.1);
        nearest(p, tree, 0, closestNeighbor, new RectHV(0d, 0d, 1d, 1d));
        return closestNeighbor.value;
    }


//    private void nearest(Point2D p, Node subtree, int deep, Neighbor closestNeighbor) {
//        updateNeighbour(p, subtree, closestNeighbor);
//        if (subtree != null) {
//            if (goLeft(p, subtree, deep)) {
//                nearest(p, subtree.left, deep + 1, closestNeighbor);
//                nearest(p, subtree.right, deep + 1, closestNeighbor);
//            } else {
//                nearest(p, subtree.right, deep + 1, closestNeighbor);
//                nearest(p, subtree.left, deep + 1, closestNeighbor);
//            }
//        }
//    }

    private void nearest(Point2D p, Node subtree, int deep, Neighbor closestNeighbor, RectHV parentRect) {
        updateNeighbour(p, subtree, closestNeighbor);

        if (subtree != null) {
            RectHV rightRect = deep % 2 == 0 ?
                    new RectHV(subtree.value.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax()) :
                    new RectHV(parentRect.xmin(), subtree.value.y(), parentRect.xmax(), parentRect.ymax());
            RectHV leftRect = deep % 2 == 0 ?
                    new RectHV(parentRect.xmin(), parentRect.ymin(), subtree.value.x(), parentRect.ymax()) :
                    new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), subtree.value.y());
            if (goLeft(p, subtree, deep)) {
                nearest(p, subtree.left, deep + 1, closestNeighbor, leftRect);
                if (rightRect.distanceSquaredTo(p) < closestNeighbor.distance) {
                    nearest(p, subtree.left, deep + 1, closestNeighbor, rightRect);
                }
            } else {
                nearest(p, subtree.right, deep + 1, closestNeighbor, rightRect);
                if (leftRect.distanceSquaredTo(p) < closestNeighbor.distance) {
                    nearest(p, subtree.left, deep + 1, closestNeighbor, leftRect);
                }
            }
        }
    }

    private void updateNeighbour(Point2D p, Node subtree, Neighbor closestNeighbor) {
        if (subtree != null) {
            double dist = p.distanceSquaredTo(subtree.value);
            if (dist < closestNeighbor.distance) {
                closestNeighbor.distance = dist;
                closestNeighbor.value = subtree.value;
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {    //         create initial board from file
//        String filename = "kdtree/circle10.txt";
//        In in = new In(filename);
//
//        // initialize the data structures with N points from standard input
//        PointSET brute = new PointSET();
//        KdTree kdtree = new KdTree();
//        while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
//            brute.insert(p);
//        }
//        System.out.println(kdtree.size());

        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();

        //circle10
        add(0.206107, 0.095492, brute, kdtree);
        add(0.975528, 0.654508, brute, kdtree);
        add(0.024472, 0.345492, brute, kdtree);
        add(0.793893, 0.095492, brute, kdtree);
        add(0.793893, 0.904508, brute, kdtree);
        add(0.975528, 0.345492, brute, kdtree);
        add(0.206107, 0.904508, brute, kdtree);
        add(0.500000, 0.000000, brute, kdtree);
        add(0.024472, 0.654508, brute, kdtree);
        add(0.500000, 1.000000, brute, kdtree);

        Point2D p = new Point2D(0.4, 0.5);
        System.out.println(kdtree.nearest(p));
        System.out.println(brute.nearest(p));
    }

    private static void add(double x, double y, PointSET brute, KdTree kdTree) {
        Point2D p = new Point2D(x, y);
        kdTree.insert(p);
        brute.insert(p);
    }


    private class Neighbor {
        private Point2D value;
        private double distance;

        public Neighbor(Point2D value, double distance) {
            this.value = value;
            this.distance = distance;
        }
    }

    private class Node {
        private Point2D value;
        private Node left;
        private Node right;
        private int size;

        public Node(Point2D value) {
            this(value, null, null);
        }

        public Node(Point2D value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }
}

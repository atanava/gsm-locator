package com.atanava.bts.util;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KdTree {
    private static class Node {
        public Point2D.Double point; // the point
        public Rectangle2D.Double rect; // the axis-aligned rectangle corresponding to this
        public Node lb; // the left/bottom subtree
        public Node rt; // the right/top subtree
        public int size;
        public double x = 0;
        public double y = 0;
        public Node(Point2D.Double p, Rectangle2D.Double rect, Node lb, Node rt) {
            super();
            this.point = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            x = p.getX();
            y = p.getY();
        }

    }
    private Node root = null;;

    public KdTree() {
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return rechnenSize(root);
    }

    private int rechnenSize(Node node) {
        if (node == null) {
            return 0;
        } else {
            return node.size;
        }
    }

    public void insert(Point2D.Double p) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            root = insertInternal(p, root, 0);
            root.rect = new Rectangle2D.Double(0, 0, 1, 1);
        } else {
            root = insertInternal(p, root, 1);
        }
    }

    // at odd level we will compare x coordinate, and at even level we will
    // compare y coordinate
    private Node insertInternal(Point2D.Double pointToInsert, Node node, int level) {
        if (node == null) {
            Node newNode = new Node(pointToInsert, null, null, null);
            newNode.size = 1;
            return newNode;
        }
        if (level % 2 == 0) {//Horizontal partition line
            if (pointToInsert.getY() < node.y) {//Traverse in bottom area of partition
                node.lb = insertInternal(pointToInsert, node.lb, level + 1);
                if(node.lb.rect == null){
                    node.lb.rect = new Rectangle2D.Double(node.rect.getMinX(), node.rect.getMinY(),
                            node.rect.getMaxX(), node.y);
                }
            } else {//Traverse in top area of partition
                if (!node.point.equals(pointToInsert)) {
                    node.rt = insertInternal(pointToInsert, node.rt, level + 1);
                    if(node.rt.rect == null){
                        node.rt.rect = new Rectangle2D.Double(node.rect.getMinX(), node.y,
                                node.rect.getMaxX(), node.rect.getMaxY());
                    }
                }
            }

        } else if (level % 2 != 0) {//Vertical partition line
            if (pointToInsert.getX() < node.x) {//Traverse in left area of partition
                node.lb = insertInternal(pointToInsert, node.lb, level + 1);
                if(node.lb.rect == null){
                    node.lb.rect = new Rectangle2D.Double(node.rect.getMinX(), node.rect.getMinY(),
                            node.x, node.rect.getMaxY());
                }
            } else {//Traverse in right area of partition
                if (!node.point.equals(pointToInsert)) {
                    node.rt = insertInternal(pointToInsert, node.rt, level + 1);
                    if(node.rt.rect == null){
                        node.rt.rect = new Rectangle2D.Double(node.x, node.rect.getMinY(),
                                node.rect.getMaxX(), node.rect.getMaxY());
                    }
                }
            }
        }
        node.size = 1 + rechnenSize(node.lb) + rechnenSize(node.rt);
        return node;
    }

    public boolean contains(Point2D.Double p) {
        return containsInternal(p, root, 1);
    }

    private boolean containsInternal(Point2D.Double pointToSearch, Node node, int level) {
        if (node == null) {
            return false;
        }
        if (level % 2 == 0) {//Horizontal partition line
            if (pointToSearch.getY() < node.y) {
                return containsInternal(pointToSearch, node.lb, level + 1);
            } else {
                if (node.point.equals(pointToSearch)) {
                    return true;
                }
                return containsInternal(pointToSearch, node.rt, level + 1);
            }
        } else {//Vertical partition line
            if (pointToSearch.getX() < node.x) {
                return containsInternal(pointToSearch, node.lb, level + 1);
            } else {
                if (node.point.equals(pointToSearch)) {
                    return true;
                }
                return containsInternal(pointToSearch, node.rt, level + 1);
            }
        }

    }

//    public void draw() {
//        StdDraw.clear();
//        drawInternal(root, 1);
//    }
//
//    private void drawInternal(Node node, int level) {
//        if (node == null) {
//            return;
//        }
//        StdDraw.setPenColor(StdDraw.BLACK);
//        StdDraw.setPenRadius(0.02);
//        node.point.draw();
//        double sx = node.rect.xmin();
//        double ex = node.rect.xmax();
//        double sy = node.rect.ymin();
//        double ey = node.rect.ymax();
//        StdDraw.setPenRadius(0.01);
//        if (level % 2 == 0) {
//            StdDraw.setPenColor(StdDraw.BLUE);
//            sy = ey = node.y;
//        } else {
//            StdDraw.setPenColor(StdDraw.RED);
//            sx = ex = node.x;
//        }
//        StdDraw.line(sx, sy, ex, ey);
//        drawInternal(node.lb, level + 1);
//        drawInternal(node.rt, level + 1);
//    }

    /**
     * Find the points which lies in the rectangle as parameter
     * @param rect
     * @return
     */
    public Iterable<Point2D.Double> range(Rectangle2D.Double rect) {
        List<Point2D.Double> resultList = new ArrayList<>();
        rangeInternal(root, rect, resultList);
        return resultList;
    }

    private void rangeInternal(Node node, Rectangle2D.Double rect, List<Point2D.Double> resultList) {
        if (node == null) {
            return;
        }
        if (node.rect.intersects(rect)) {
            if (rect.contains(node.point)) {
                resultList.add(node.point);
            }
            rangeInternal(node.lb, rect, resultList);
            rangeInternal(node.rt, rect, resultList);
        }
    }

    public Point2D.Double nearest(Point2D.Double p) {
        if(root == null){
            return null;
        }
        Champion champion = new Champion(root.point,Double.MAX_VALUE);
        return nearestInternal(p, root, champion, 1).champion;
    }

    private Champion nearestInternal(Point2D.Double targetPoint, Node node,
                                     Champion champion, int level) {
        if (node == null) {
            return champion;
        }
        double dist = targetPoint.distanceSq(node.point);
        int newLevel = level + 1;
        if (dist < champion.championDist) {
            champion.champion = node.point;
            champion.championDist = dist;
        }
        boolean goLeftOrBottom = false;
        //We will decide which part to be visited first, based upon in which part point lies.
        //If point is towards left or bottom part, we traverse in that area first, and later on decide
        //if we need to search in other part too.
        if(level % 2 == 0){
            if(targetPoint.getY() < node.y) {
                goLeftOrBottom = true;
            }
        } else {
            if(targetPoint.getX() < node.x) {
                goLeftOrBottom = true;
            }
        }
        if(goLeftOrBottom){
            nearestInternal(targetPoint, node.lb, champion, newLevel);
            Point2D.Double orientationPoint = createOrientationPoint(node.x,node.y,targetPoint,level);
            double orientationDist = orientationPoint.distanceSq(targetPoint);
            //We will search on the other part only, if the point is very near to partitioned line
            //and champion point found so far is far away from the partitioned line.
            if(orientationDist < champion.championDist){
                nearestInternal(targetPoint, node.rt, champion, newLevel);
            }
        } else {
            nearestInternal(targetPoint, node.rt, champion, newLevel);
            Point2D.Double orientationPoint = createOrientationPoint(node.x,node.y,targetPoint,level);
            //We will search on the other part only, if the point is very near to partitioned line
            //and champion point found so far is far away from the partitioned line.
            double orientationDist = orientationPoint.distanceSq(targetPoint);
            if(orientationDist < champion.championDist){
                nearestInternal(targetPoint, node.lb, champion, newLevel);
            }

        }
        return champion;
    }
    /**
     * Returns the point from a partitioned line, which can be directly used to calculate
     * distance between partitioned line and the target point for which neighbours are to be searched.
     * @param linePointX
     * @param linePointY
     * @param targetPoint
     * @param level
     * @return
     */
    private Point2D.Double createOrientationPoint(double linePointX, double linePointY, Point2D.Double targetPoint, int level){
        if(level % 2 == 0){
            return new Point2D.Double(targetPoint.getX(),linePointY);
        } else {
            return new Point2D.Double(linePointX,targetPoint.getY());
        }
    }

    private static class Champion {
        public Point2D.Double champion;
        public double championDist;
        public Champion(Point2D.Double c, double d){
            champion = c;
            championDist = d;
        }
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        Random random = new Random();
        random.doubles(99, 1.0, 300.0).forEach(d -> kdTree.insert(new Point2D.Double(d, random.doubles(1, 1.0, 300.0).findAny().getAsDouble())));
        // kdTree.print();
        System.out.println(kdTree.size());
//        kdTree.draw();
        System.out.println(kdTree.nearest(new Point2D.Double(1.4, 10.5)));
        System.out.println(new Point2D.Double(10.7, 10.4).distanceSq(new Point2D.Double(10.9,10.5)));
        System.out.println(new Point2D.Double(10.7, 10.4).distanceSq(new Point2D.Double(10.9,10.4)));

    }
}
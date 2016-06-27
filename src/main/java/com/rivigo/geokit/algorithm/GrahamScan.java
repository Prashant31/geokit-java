package com.rivigo.geokit.algorithm;

import com.rivigo.geokit.model.LatLng;
import com.rivigo.geokit.model.Polygon;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Created by prashant on 6/26/16.
 */

@Slf4j
public class GrahamScan {

    protected static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }

    protected static boolean areAllCollinear(List<LatLng> points) {

        if(points.size() < 2) {
            return true;
        }

        final LatLng a = points.get(0);
        final LatLng b = points.get(1);

        for(int i = 2; i < points.size(); i++) {

            LatLng c = points.get(i);

            if(getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }

        return true;
    }

    public static Polygon getPolygon(List<LatLng> points) throws IllegalArgumentException {
        List<LatLng> polygonPoints = getPolygonSequence(points);
        return new Polygon(polygonPoints);
    }

    public static LatLng getCentroid (List <LatLng> points) throws IllegalArgumentException {
        List<LatLng> polygonPoints = getPolygonSequence(points);
        Polygon polygon = new Polygon(polygonPoints);
        return polygon.centroid();
    }


    public static List<LatLng> getPolygonSequence(List<LatLng> points) throws IllegalArgumentException {

        List<LatLng> sorted = new ArrayList<LatLng>(getSortedPointSet(points));

        if(sorted.size() < 3) {
            throw new IllegalArgumentException("can only create a convex hull of 3 or more unique points");
        }

        if(areAllCollinear(sorted)) {
            throw new IllegalArgumentException("cannot create a convex hull from collinear points");
        }

        Stack<LatLng> stack = new Stack<LatLng>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {

            LatLng head = sorted.get(i);
            LatLng middle = stack.pop();
            LatLng tail = stack.peek();

            Turn turn = getTurn(tail, middle, head);

            switch(turn) {
                case COUNTER_CLOCKWISE:
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(head);
                    break;
            }
        }

        // close the hull
        stack.push(sorted.get(0));

        return new ArrayList<LatLng>(stack);
    }


    protected static Set<LatLng> getSortedPointSet(List<LatLng> points) {

        final LatLng lowest = getLowestPoint(points);

        Set<LatLng> set = new TreeSet<LatLng>(new Comparator<LatLng>() {
            @Override
            public int compare(LatLng a, LatLng b) {

                if(a.equals(b)) {
                    return 0;
                }

                double thetaA = Math.atan2(a.getLng() - lowest.getLng(), a.getLat() - lowest.getLat());
                double thetaB = Math.atan2(b.getLng() - lowest.getLng(), b.getLat() - lowest.getLat());

                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    // collinear with the 'lowest' point, let the point closest to it come first

                    double distanceA = Math.sqrt(Math.pow(lowest.getLat() - a.getLat(), 2)
                            + Math.pow(lowest.getLng() - a.getLng(), 2));
                    double distanceB = Math.sqrt(Math.pow(lowest.getLat() - b.getLat(), 2)
                            + Math.pow(lowest.getLng() - b.getLng(), 2));
                    if(distanceA < distanceB) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(points);

        return set;
    }


    protected static LatLng getLowestPoint(List<LatLng> points) {

        LatLng lowest = points.get(0);

        for(int i = 1; i < points.size(); i++) {

            LatLng temp = points.get(i);

            if(temp.getLng() < lowest.getLng() || (temp.getLng() == lowest.getLng() && temp.getLat() < lowest.getLat())) {
                lowest = temp;
            }
        }

        return lowest;
    }


    protected static Turn getTurn(LatLng a, LatLng b, LatLng c) {

        // use longs to guard against int-over/underflow
        Double crossProduct = ((b.getLat() - a.getLat()) * (c.getLng() - a.getLng()))
                - ((b.getLng() - a.getLng()) * (c.getLat() - a.getLat()));

        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }
}

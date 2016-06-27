package com.rivigo.geokit.algorithm;

import com.rivigo.geokit.LatLng;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by prashantpal on 26/06/16.
 */
public class GrahamScanTest {

    @Test
    public void areAllCollinearTest() {
        /*
            6 |       d   b
            5 |         f
            4 |   a   e
            3 |     c
            2 |
            1 |
            0 '------------
              0 1 2 3 4 5 6
        */
        LatLng a = new LatLng(2d, 4d);
        LatLng b = new LatLng(6d, 6d);
        LatLng c = new LatLng(3d, 3d);
        LatLng d = new LatLng(4d, 6d);
        LatLng e = new LatLng(4d, 4d);
        LatLng f = new LatLng(5d, 5d);

        assertThat(GrahamScan.areAllCollinear(Arrays.asList(c)), is(true));
        assertThat(GrahamScan.areAllCollinear(Arrays.asList(c, e)), is(true));
        assertThat(GrahamScan.areAllCollinear(Arrays.asList(c, e, f)), is(true));
        assertThat(GrahamScan.areAllCollinear(Arrays.asList(c, b, e, e, e, f, c)), is(true));
        assertThat(GrahamScan.areAllCollinear(Arrays.asList(a, b, d)), is(false));
    }

    @Test
    public void getConvexHullTest() {

        /*
            6 |       d
            5 |     b   g
            4 |   a   e   i
            3 |     c   h
            2 |       f
            1 |
            0 '------------
              0 1 2 3 4 5 6
        */
        LatLng a = new LatLng(2d, 4d);
        LatLng b = new LatLng(3d, 5d);
        LatLng c = new LatLng(3d, 3d);
        LatLng d = new LatLng(4d, 6d);
        LatLng e = new LatLng(4d, 4d);
        LatLng f = new LatLng(4d, 2d);
        LatLng g = new LatLng(5d, 5d);
        LatLng h = new LatLng(5d, 3d);
        LatLng i = new LatLng(6d, 4d);

        List<LatLng> convexHull = GrahamScan.getPolygonSequence(Arrays.asList(a, b, c, d, e, f, g, h, i));

        assertThat(convexHull.size(), is(5));

        assertThat(convexHull.get(0), is(f));
        assertThat(convexHull.get(1), is(i));
        assertThat(convexHull.get(2), is(d));
        assertThat(convexHull.get(3), is(a));
        assertThat(convexHull.get(4), is(f));

        /*
            6       |       d   m
            5       |     b   g
            4       |   a   e   i
            3 j     |     c   h
            2       |       f
            1       |
            0       '------------
           -1
           -2                   k
           -3
              -2 -1 0 1 2 3 4 5 6
        */
        LatLng j = new LatLng(-2d, 3d);
        LatLng k = new LatLng(6d, -2d);
        LatLng m = new LatLng(6d, 6d);

        convexHull = GrahamScan.getPolygonSequence(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, m));

        assertThat(convexHull.size(), is(5));

        assertThat(convexHull.get(0), is(k));
        assertThat(convexHull.get(1), is(m));
        assertThat(convexHull.get(2), is(d));
        assertThat(convexHull.get(3), is(j));
        assertThat(convexHull.get(4), is(k));

        /*
            large   |                         m
            .       |
            .       |
            7  j    |
            6       |       d
            5       |     b   g
            4       |   a   e   i
            3       |     c   h
            2       |       f
            1       |
            0       '--------------------------
           -1
           -2                       k
           -3
              -2 -1 0 1 2 3 4 5 6 7 8 . . large
        */
        j = new LatLng(-2d, 7d);
        k = new LatLng(8d, -2d);
        m = new LatLng(Double.MAX_VALUE, Double.MAX_VALUE);

        convexHull = GrahamScan.getPolygonSequence(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, m));

        assertThat(convexHull.size(), is(4));

        assertThat(convexHull.get(0), is(k));
        assertThat(convexHull.get(1), is(m));
        assertThat(convexHull.get(2), is(j));
        assertThat(convexHull.get(3), is(k));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getConvexHullTestFail() {
        /*
            6 |
            5 |
            4 |   a
            3 |     c
            2 |       f
            1 |
            0 '------------
              0 1 2 3 4 5 6
        */
        LatLng a = new LatLng(2d, 4d);
        LatLng b = new LatLng(3d, 3d);
        LatLng c = new LatLng(4d, 2d);

        GrahamScan.getPolygonSequence(Arrays.asList(a, b, c));
    }

    @Test
    public void getLowestLatLngTest() {

        /*
            6    |       d
            5    |     b   g
            4    |   a   e   i
            3    |     c   h
            2    |       f
            1    |
            0    '------------
           -1
           -2
              -1 0 1 2 3 4 5 6
        */
        LatLng a = new LatLng(2d, 4d);
        LatLng b = new LatLng(3d, 5d);
        LatLng c = new LatLng(3d, 3d);
        LatLng d = new LatLng(4d, 6d);
        LatLng e = new LatLng(4d, 4d);
        LatLng f = new LatLng(4d, 2d);
        LatLng g = new LatLng(5d, 5d);
        LatLng h = new LatLng(5d, 3d);
        LatLng i = new LatLng(6d, 4d);

        LatLng lowest = GrahamScan.getLowestPoint(Arrays.asList(a, b, c, d, e, f, g, h, i));

        assertThat(lowest, is(f));

        /*
            6    |       d
            5    |     b   g
            4    |   a   e   i
            3    |     c   h
            2    |       f
            1    |
            0    '------------
           -1  j             k
           -2
              -1 0 1 2 3 4 5 6
        */
        LatLng j = new LatLng(-1d, -1d);
        LatLng k = new LatLng(6d, -1d);

        lowest = GrahamScan.getLowestPoint(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k));

        assertThat(lowest, is(j));
    }

    @Test
    public void getSortedLatLngSetTest() {

        /*
            6    |
            5    |
            4    |
            3    |
            2    g   a
            1    f b
            0    c-e-d--------
           -1
           -2
              -1 0 1 2 3 4 5 6
        */
        LatLng a = new LatLng(2d, 2d);
        LatLng b = new LatLng(1d, 1d);
        LatLng c = new LatLng(0d, 0d);
        LatLng d = new LatLng(2d, 0d);
        LatLng e = new LatLng(1d, 0d);
        LatLng f = new LatLng(0d, 1d);
        LatLng g = new LatLng(0d, 2d);
        LatLng h = new LatLng(2d, 2d); // duplicate
        LatLng i = new LatLng(2d, 2d); // duplicate

        List<LatLng> points = Arrays.asList(a, b, c, d, e, f, g, h, i);

        Set<LatLng> set = GrahamScan.getSortedPointSet(points);
        LatLng[] array = set.toArray(new LatLng[set.size()]);

        assertThat(set.size(), is(7));

        assertThat(array[0], is(c));
        assertThat(array[1], is(e));
        assertThat(array[2], is(d));
        assertThat(array[3], is(b));
        assertThat(array[4], is(a));
        assertThat(array[5], is(f));
        assertThat(array[6], is(g));
    }

    @Test
    public void getTurnTest() {

        /*
            9       |             d
            8       |               c
            7       |                 e
            6       |
            5       |
            4       |       b
            3       |
            2       |
            1   h   | a
            0     g '------------------
           -1       f
           -2
              -2 -1 0 1 2 3 4 5 6 7 8 9
        */
        LatLng a = new LatLng(1d, 1d);
        LatLng b = new LatLng(4d, 4d);
        LatLng c = new LatLng(8d, 8d);
        LatLng d = new LatLng(7d, 9d);
        LatLng e = new LatLng(9d, 7d);
        LatLng f = new LatLng(0d, -1d);
        LatLng g = new LatLng(-1d, 0d);
        LatLng h = new LatLng(-2d, 1d);

        assertThat(GrahamScan.getTurn(a, b, c), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(a, c, b), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(b, a, c), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(c, b, a), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(e, d, c), is(GrahamScan.Turn.COLLINEAR));
        assertThat(GrahamScan.getTurn(h, f, g), is(GrahamScan.Turn.COLLINEAR));

        assertThat(GrahamScan.getTurn(a, b, e), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, b, f), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, c, e), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, c, f), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(c, b, g), is(GrahamScan.Turn.CLOCKWISE));
        assertThat(GrahamScan.getTurn(d, b, f), is(GrahamScan.Turn.CLOCKWISE));

        assertThat(GrahamScan.getTurn(a, b, d), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, e, d), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(e, c, f), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(b, d, a), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(a, g, f), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
        assertThat(GrahamScan.getTurn(f, b, a), is(GrahamScan.Turn.COUNTER_CLOCKWISE));
    }
}

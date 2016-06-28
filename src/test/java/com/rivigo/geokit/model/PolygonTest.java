package com.rivigo.geokit.model;

import junit.framework.TestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by prashant on 6/28/16.
 */
public class PolygonTest extends TestCase{

    protected LatLng p1;
    protected LatLng p2;
    protected LatLng p3;
    protected LatLng p4;
    protected LatLng p5;

    protected LatLng[] points;
    protected Polygon polygon;

    protected LatLng pointInside;
    protected LatLng pointOutside;

    protected LatLng c1;
    protected LatLng c2;
    protected LatLng c3;
    protected LatLng c4;
    protected LatLng c5;
    protected LatLng c6;
    protected LatLng c7;
    protected LatLng c8;
    protected LatLng c9;
    protected LatLng c10;
    protected LatLng c11;

    protected LatLng[] complexPoints;
    protected Polygon complexPolygon;

    protected LatLng complexInsideOne;
    protected LatLng complexInsideTwo;
    protected LatLng complexInsideThree;

    protected LatLng complexOutsideOne;
    protected LatLng complexOutsideTwo;
    protected LatLng complexOutsideThree;


    protected LatLng op1;
    protected LatLng op2;
    protected LatLng op3;
    protected LatLng op4;

    protected LatLng[] openPoints;
    protected Polygon openPolygon;

    @Override
    protected void setUp(){
        //Create a simple square-ish polygon for easy testing
        this.p1 = new LatLng(45.3142533036254, -93.47527313511819);
        p2 = new LatLng(45.31232182518015, -93.34893036168069);
        p3 = new LatLng(45.23694281999268, -93.35167694371194);
        p4 = new LatLng(45.23500870841669, -93.47801971714944);
        p5 = new LatLng(45.3142533036254, -93.47527313511819);

        points = new LatLng[]{p1, p2, p3, p4, p5};
        polygon = new Polygon(points);

        pointInside = new LatLng(45.27428243796789, -93.41648483416066);
        pointOutside = new LatLng(45.45411010558687, -93.78151703160256);

        //Create a more complex polygon with overlapping lines.  Looks like a star of david
        c1 = new LatLng(45.48661334374487, -93.74665833078325);
        c2 = new LatLng(45.53521281284293, -93.32611083984375);
        c3 = new LatLng(45.28648197278281, -93.3673095703125);
        c4 = new LatLng(45.31497759107127, -93.75764465890825);
        c5 = new LatLng(45.36179519142128, -93.812255859375);
        c6 = new LatLng(45.40230699238177, -93.74908447265625);
        c7 = new LatLng(45.236217535866025, -93.60076904296875);
        c8 = new LatLng(45.39989638818863, -93.282485967502);
        c9 = new LatLng(45.565986795411376, -93.5760498046875);
        c10 = new LatLng(45.4345991655272, -93.73017883859575);
        c11 = new LatLng(45.48661334374487, -93.74665833078325);

        complexPoints = new LatLng[]{c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11};
        complexPolygon = new Polygon(complexPoints);

        //Test three points that should be "inside" this complex shape
        complexInsideOne = new LatLng(45.52438983143154, -93.59818268101662);
        complexInsideTwo = new LatLng(45.50321887154943, -93.37845611851662);
        complexInsideThree = new LatLng(45.28334174918666, -93.59543609898537);

        //Test three points that should be "outside" this complex shape
        complexOutsideOne = new LatLng(45.45314676076135, -93.563850405626);
        complexOutsideTwo = new LatLng(45.30435378077673, -93.6859130859375);
        complexOutsideThree = new LatLng(45.538820010517036, -93.486946108751);

        //Test open sided polygon aka line - for closing on initialize
        op1 = new LatLng(44.97402795596173, -92.7297592163086);
        op2 = new LatLng(44.97395509241393, -92.68448066781275);
        op3 = new LatLng(44.94455954512172, -92.68413734505884);
        op4 = new LatLng(44.94383053857761, -92.72876930306666);

        openPoints = new LatLng[]{op1, op2, op3, op4};
        openPolygon = new Polygon(openPoints);
    }


    public void testPointInsidePoly(){
        assertThat(polygon.contains(pointInside) , is(true));
    }

    public void testPointOutsidePoly(){
        assertThat(polygon.contains(pointOutside), is(false));
    }

    public void testPointsInsideComplexPoly(){
        assertThat(complexPolygon.contains(complexInsideOne), is(true));
        assertThat(complexPolygon.contains(complexInsideTwo), is(true));
        assertThat(complexPolygon.contains(complexInsideThree), is(true));
    }

    public void testPointsOutsideComplexPoly(){
        assertThat(complexPolygon.contains(complexOutsideOne), is(false));
        assertThat(complexPolygon.contains(complexOutsideTwo), is(false));
        assertThat(complexPolygon.contains(complexOutsideThree), is(false));
    }

    public void testOpenPolygon(){
        // A polygon can only exist of the last point is equal to the first
        // Otherwise, it would just be a line of points.
        assertThat(openPolygon.getPoints().get(0).getLng().equals(openPolygon.getPoints().get(openPolygon.getPoints().size() -1).getLng())
                , is(true));
        assertThat(openPolygon.getPoints().get(0).getLat().equals(openPolygon.getPoints().get(openPolygon.getPoints().size() -1).getLat())
                , is(true));

    }

    public void testCentroidForSimplePoly(){
        LatLng polygonCentroid = new LatLng(45.27463866133501, -93.41400121829719);
        assertThat(polygon.centroid().equals(polygonCentroid), is(true));
    }

    public void testCentroidForComplexPoly(){
        LatLng complexPolygonCentroid = new LatLng(45.43622702936517, -93.5352210389731);
        assertThat(complexPolygon.centroid().equals(complexPolygonCentroid), is(true));
    }

    public void testCentroidForOpenPoly(){
        LatLng openPolygonCentroid = new LatLng(44.95912726688109, -92.7068888186181);
        assertThat(openPolygon.centroid().equals(openPolygonCentroid), is(true));
    }

}

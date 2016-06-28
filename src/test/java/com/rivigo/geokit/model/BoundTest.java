package com.rivigo.geokit.model;

import junit.framework.TestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by prashant on 6/28/16.
 */
public class BoundTest extends TestCase {

    protected LatLng sw;
    protected LatLng ne;
    protected Bound bound;

    protected LatLng locA;
    protected LatLng locB;

    Bound crossMeridian;
    LatLng insideCM;
    LatLng insideCM2;
    LatLng eastOfCm;
    LatLng westOfCm;

    @Override
    public void setUp() {
        // This is the area in Texas
        sw = new LatLng(32.91663, -96.982841);
        ne = new LatLng(32.96302, -96.919495);
        bound = new Bound(sw, ne);
        locA = new LatLng(32.918593, -96.958444); // inside bounds
        locB = new LatLng(32.914144, -96.958444); // outside bouds

        // this is a cross-meridan area
        crossMeridian = new Bound(new LatLng(30d, 170d), new LatLng(40d, -170d));
        insideCM = new LatLng(35d, 175d);
        insideCM2 = new LatLng(35d, -175d);
        eastOfCm = new LatLng(35d, -165d);
        westOfCm = new LatLng(35d, 165d);
    }

    public void testEquality() {
        assertThat((new Bound(sw, ne)).equals(new Bound(sw, ne)), is(true));
    }

    public void testPointInsideBounds() {
        assertThat(bound.contains(locA), is(true));
    }

    public void testPointOutsideBounds() {
        assertThat(bound.contains(locB), is(false));
    }

    public void testPointInsideBoundsCrossMeridian() {
        assertThat(crossMeridian.contains(insideCM), is(true));
        assertThat(crossMeridian.contains(insideCM2), is(true));
    }

    public void testPointOutsideBoundsCrossMeridian() {
        assertThat(!crossMeridian.contains(eastOfCm), is(true));
        assertThat(!crossMeridian.contains(westOfCm), is(true));
    }

    public void testCreationFromCircle() {
        bound = Bound.fromPointAndRadius(new LatLng(32.939829, -96.951176), 2.5);
        LatLng inside = new LatLng(32.9695270000, -96.9901590000);
        LatLng outside = new LatLng(32.8951550000, -96.9584440000);
        assertThat(bound.contains(inside), is(true));
        assertThat(bound.contains(outside), is(false));
    }

}


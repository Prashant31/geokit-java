package com.rivigo.geokit.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashant on 6/26/16.
 */
public class LatLngTest extends TestCase {

    LatLng locA = new LatLng(32.918593, -96.958444);
    LatLng locB = new LatLng(32.969527, -96.990159);
    LatLng locC = new LatLng(locA.getLat(), locA.getLng());

    protected void setUp(){

    }

    @Test
    public void testSameDistance(){
        assertEquals(true, locA.equals(locA));
    }

    @Test
    public void testDistanceBetweenSame(){
        assertEquals(0d, locA.distanceTo(locA));
    }

    @Test
    public void testDistanceBetweenSameWithMilesAndFlat(){
        Map<String , String> options = new HashMap<>();
        options.put("formula", "flat");
        assertEquals(0d, locA.distanceBetween(locA, options));
        options.put("formula", "sphere");
        assertEquals(0d, locA.distanceBetween(locA, options));
    }

}

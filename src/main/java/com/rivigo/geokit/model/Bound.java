package com.rivigo.geokit.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Created by prashant on 6/25/16.
 */

@Getter
@Setter
@NoArgsConstructor
public class Bound {

    private LatLng ne;
    private LatLng sw;

    public Bound(LatLng sw, LatLng ne){
        this.ne = ne;
        this.sw = sw;
    }

    public LatLng center(){
        return sw.midpointTo(ne);
    }

    /**
     *
     * returns an instance of bounds which completely encompases the given circle
     *
     * @param point
     * @param radius
     * @param options
     * @return
     */
    public static Bound fromPointAndRadius(LatLng point, Double radius, Map<String, String>options){
        LatLng p0 = point.endpoint(0d, radius, options);
        LatLng p90 = point.endpoint(90d, radius, options);
        LatLng p180 = point.endpoint(180d, radius, options);
        LatLng p270 = point.endpoint(270d, radius, options);
        LatLng southWest = new LatLng(p180.getLat(), p270.getLng());
        LatLng northEast = new LatLng(p0.getLat(), p90.getLng());
        return new Bound(southWest, northEast);
    }

    /**
     *
     * returns an instance of bounds which completely encompases the given circle
     *
     * @param point
     * @param radius
     * @param options
     * @return
     */
    public static Bound fromPointAndRadius(LatLng point, Double radius){
       return fromPointAndRadius(point,radius,null);
    }

    public Boolean contains(LatLng point){
        Boolean res = point.getLat() > sw.getLat() && point.getLat() < ne.getLat();
        if(crossesMeridian()){
            res = res && (point.getLng() < ne.getLng() || point.getLng() > sw.getLng());
        }else{
            res = res && (point.getLng() < ne.getLng() && point.getLng() > sw.getLng());
        }
        return res;
    }

    /**
     * returns true if the bounds crosses the international dateline
     * @return
     */
    private Boolean crossesMeridian(){
        return  sw.getLng() > ne.getLng();
    }

    /**
     * Returns true if the candidate object is logically equal. Logical
     * equivalence is true if the lat and lng attributes are the same for both
     * @param target
     * @return
     */
    @Override
    public boolean equals(Object target){
        Bound bound = (Bound) target;
        return (sw.equals(bound.getSw()) && ne.equals(bound.getNe()));
    }
}

package com.rivigo.geokit.model;

import com.rivigo.geokit.model.base.Mappable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Map;

/**
 * Created by prashant on 6/25/16.
 */

@Getter
@Setter
@NoArgsConstructor
public class LatLng extends Mappable implements Comparable{

    private Double lat;
    private Double lng;

    public LatLng(Double lat, Double lng){
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Generates the LatLng object from a json
     * Json String should contain fields namely lat and long
     * @param jsonString
     */
    public LatLng (String jsonString){
        JSONObject jsonObject = new JSONObject(new JSONTokener(jsonString));
        this.lat = jsonObject.getDouble("lat");
        this.lng = jsonObject.getDouble("lng");
    }

    /**
     * Get distance between two LatLng objects
     * Takes an options HashMap as well
     * Valid keys for hash map are unit anf formula
     * Other entries are if ignored
     * Valid values for formula are "flat" and "sphere"
     * Valid Values for unit are "kms", "miles"
     * @param other
     * @param options
     * @return
     */
    public Double distanceTo(LatLng other, Map<String, String> options){
        return distanceBetween(this, other, options);
    }

    /**
     *
     */

    public Double distanceTo(LatLng other){
        return distanceBetween(this, other, null);
    }

    /**
     *
     */

    public Double distanceBetween(LatLng other){
        return distanceBetween(this, other);
    }

    /**
     *
     */

    public Double distanceBetween(LatLng other, Map<String,String> options){
        return distanceBetween(this, other, options);
    }

    /**
     * It behaves as an alias to distance To
     * logically both means the same thing but logically both method names should
     * be provided
     * @param other
     * @param options
     * @return
     */
    public Double distanceFrom(LatLng other, Map<String, String> options){
        return distanceTo(other,options);
    }

    /**
     * Returns heading in degrees (0 is north, 90 is east, 180 is south, etc) to
     * the given point.
     * @param other
     * @return
     */
    public Double headingTo(LatLng other){
        return heading_between(this, other);
    }

    /**
     * Returns an array representation of the LatLng
     * @return
     */
    public Double[] toArray(){
        Double[] arr = new Double[2];
        arr[0] = lat;
        arr[1] = lng;
        return arr;
    }

    //TODO => Improve this function. Should be better than just a null check??

    /**
     * Returns true if both lat and lng
     * have non null values
     * @return
     */
    public Boolean valid(){
        return (lat != null && lng != null);
    }

    /**
     * Returns the endpoint, given a heading (in degrees) and distance.
     * Takes an options HashMap as well
     * Valid keys for hash map are unit anf formula
     * Other entries are if ignored
     * Valid values for formula are "flat" and "sphere"
     * Valid Values for unit are "kms", "miles"
     * @param heading
     * @param distance
     * @param options
     * @return
     */
    public LatLng endpoint(Double heading, Double distance, Map<String, String> options){
        return endpoint(this, heading, distance, options);
    }

    /**
     * Gives the mid point between two LatLng
     * @param other
     * @return
     */
    public LatLng midpointTo(LatLng other){
        return midpoint_between(this, other, null);
    }

    /**
     *
     * Gives the mid point between two LatLng
     * Valid keys for hash map are unit anf formula
     * Other entries are if ignored
     * Valid values for formula are "flat" and "sphere"
     * Valid Values for unit are "kms", "miles"
     *
     * @param other
     * @param options
     * @return
     */
    public LatLng midpointTo(LatLng other, Map<String, String> options){
        return midpoint_between(this, other, options);
    }

    @Override
    public boolean equals(Object object){
        LatLng target = (LatLng) object;
        return (lat.equals(target.getLat()) && lng.equals(target.getLng()));
    }


    @Override
    public int compareTo (Object object) {
        LatLng o = (LatLng) object;
        return compareFunction(o);
    }

    public int compare(Object object){
        return compareTo(object);
    }


    private int compareFunction(LatLng o){
        if (lat.compareTo(o.getLat()) < 0)
            return -1;
        else if (lat.compareTo(o.getLat()) > 0)
            return 1;
        else if (lng.compareTo(o.getLng()) < 0)
            return -1;
        else if (lng.compareTo(o.getLng()) > 0)
            return 1;
        else
            return 0;
    }

}

package com.rivigo.geokit.model;

import com.rivigo.geokit.model.base.Mappable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashant on 6/25/16.
 */

@Getter
@Setter
@NoArgsConstructor
public class LatLng extends Mappable{

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
    public Double heading_to(LatLng other){
        return heading_between(this, other);
    }

    /**
     * Returns an array representation of the LatLng
     * @return
     */
    public Double[] to_a(){
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
    public LatLng midpoint_to(LatLng other){
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
    public LatLng midpoint_to(LatLng other, Map<String, String> options){
        return midpoint_between(this, other, options);
    }

    @Override
    public boolean equals(Object object){
        LatLng target = (LatLng) object;
        return (lat == target.lat && lng == target.lng);
    }

}

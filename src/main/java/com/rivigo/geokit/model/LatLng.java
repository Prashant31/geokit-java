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

    public LatLng (String jsonString){
        JSONObject jsonObject = new JSONObject(new JSONTokener(jsonString));
        this.lat = jsonObject.getDouble("lat");
        this.lng = jsonObject.getDouble("lng");
    }

    public Double distanceTo(LatLng other, Map<String, String> options){
        return distanceBetween(this, other, options);
    }

    public Double distanceFrom(LatLng other, Map<String, String> options){
        return distanceTo(other,options);
    }


    public LatLng midpointTo(LatLng other, Map<String ,String>options){
        return midpoint_between(this, other, options);
    }

    public Double heading_to(LatLng other){
        return heading_between(this, other);
    }

    public Double[] to_a(){
        Double[] arr = new Double[2];
        arr[0] = lat;
        arr[1] = lng;
        return arr;
    }

    public Boolean valid(){
        return (lat != null && lng != null);
    }

    public LatLng endpoint(Double heading, Double distance, Map<String, String> options){
        return endpoint(this, heading, distance, options);
    }


    public LatLng midpoint_to(LatLng other){
        return midpoint_between(this, other, null);
    }

    public LatLng midpoint_to(LatLng other, Map<String, String> options){
        return midpoint_between(this, other, options);
    }

    @Override
    public boolean equals(Object object){
        LatLng target = (LatLng) object;
        return (lat == target.lat && lng == target.lng);
    }

}

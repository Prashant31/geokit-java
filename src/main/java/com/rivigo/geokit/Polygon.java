package com.rivigo.geokit;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by prashant on 6/25/16.
 */

@Getter
@Setter
public class Polygon {

    private List<LatLng> points;


    public Polygon(List<LatLng> points){
        this.points = points;
        if(!(points.get(0).equals(points.get(points.size() -1 )))){
            this.points.add(points.get(0));
        }
    }

    /**
     * Checks if a point i.e LatLng Object lies in between the polygon
     *
     * @param point
     * @return
     */
    public Boolean contains(LatLng point){
        LatLng lastPoint = this.points.get(this.points.size()-1);
        Boolean oddNodes = false;
        Double x = point.getLng();
        Double y = point.getLat();
        for(LatLng polygonPoint : this.points){
            Double yi = polygonPoint.getLat();
            Double xi = polygonPoint.getLng();
            Double yj = lastPoint.getLat();
            Double xj = lastPoint.getLng();
            if( yi < y && yj >= y || yj < y && yi >= y){
                if (xi + (y - yi) / (yj - yi) * (xj - xi) < x){
                    oddNodes = !oddNodes;
                }
            }
            lastPoint = polygonPoint;
        }
        return oddNodes;
    }


    /**
     * Calculates the centroid of the polygon
     * @return
     */
    public LatLng centroid(){
        Double centroidLat = 0.0;
        Double centroidLng = 0.0;
        Double signedArea = 0.0;
        //Iterate over each element in the list but the last item as it's
        //calculated by the i+1 logic

        for(int i=0; i<this.points.size()-1; i++){
            Double x0 = points.get(i).getLat();
            Double y0 = points.get(i).getLng();
            Double x1 = points.get(i + 1).getLat();
            Double y1 = points.get(i + 1).getLng();
            Double a = (x0 * y1) - (x1 * y0);
            signedArea += a;
            centroidLat += (x0 + x1) * a;
            centroidLng += (y0 + y1) * a;
        }
        signedArea *= 0.5;
        centroidLat /= (6.0 * signedArea);
        centroidLng /= (6.0 * signedArea);
        return new LatLng(centroidLat, centroidLng);

    }
}

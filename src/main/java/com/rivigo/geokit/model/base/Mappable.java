package com.rivigo.geokit.model.base;

import com.rivigo.geokit.exceptions.UnsupportedUnitException;
import com.rivigo.geokit.model.LatLng;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Created by prashant on 6/25/16.
 */
@Slf4j
public class Mappable {

    private static final Double PI = Math.PI;
    private static final Double PI_DIV_RAD = Math.PI / 180;

    private static final  String[] SUPPORTED_UNITS = new String[]{"miles", "kms"};
    private static final  String[] SUPPORTED_FORMULAE = new String[]{"sphere", "flat"};
    private static final String DEFAULT_UNIT = "kms";
    private static final String DEFAULT_FORMULA =  "sphere";

    private static final Double EARTH_RADIUS_IN_METERS = 6376772.71d;
    private static final Double METERS_PER_LATITUDE_DEGREE = 111181.9d;

    private static Map<String , Double> EARTH_RADIUS = new HashMap<>();
    private static Map<String , Double> PER_LATITUDE_DEGREE = new HashMap<>();

    public Mappable(){
        EARTH_RADIUS.put("meters", EARTH_RADIUS_IN_METERS * 1);
        EARTH_RADIUS.put("kms", EARTH_RADIUS_IN_METERS * 1/1000d);
        EARTH_RADIUS.put("miles", EARTH_RADIUS_IN_METERS * 1/1609d);
        EARTH_RADIUS.put("nms", EARTH_RADIUS_IN_METERS *  0.0005400722448725917);
        PER_LATITUDE_DEGREE.put("meters", METERS_PER_LATITUDE_DEGREE * 1);
        PER_LATITUDE_DEGREE.put("kms", METERS_PER_LATITUDE_DEGREE * 1/1000d);
        PER_LATITUDE_DEGREE.put("miles", METERS_PER_LATITUDE_DEGREE * 1/1609d);
        PER_LATITUDE_DEGREE.put("nms", METERS_PER_LATITUDE_DEGREE *  0.0005400722448725917);
    }


    protected LatLng midpointBetween(LatLng from, LatLng to, Map<String ,String >options){
        Double heading = from.headingTo(to);
        Double distance = from.distanceTo(to, options);
        return from.endpoint(heading, distance / 2, options);
    }



     protected Double headingBetween(LatLng from, LatLng to){
        Double degreeLng = deg2rad(to.getLng() - from.getLng());
        Double fromLat = deg2rad(from.getLat());
        Double toLat = deg2rad(to.getLat());
        Double y = Math.sin(degreeLng) * Math.cos(toLat);
        Double x = Math.cos(fromLat) * Math.sin(toLat) -
                Math.sin(fromLat) * Math.cos(toLat) * Math.cos(degreeLng);
        return toHeading(Math.atan2(y, x));
    }

    public Double distanceBetween(LatLng from, LatLng to){
        return this.distanceBetween(from,to,null,null);
    }

    public Double distanceBetween(LatLng from, LatLng to, Map<String,String > options){
        Map<String, String > optionMap = this.getOptions(options);
        return this.distanceBetween(from, to, optionMap.get("unit"), optionMap.get("formula"));
    }

    private Double distanceBetween(LatLng from, LatLng to, String unit, String formula){
        String applicableUnit = this.getUnit(unit);
        if(from.equals(to)){
            return 0d;
        }else {
            formula = this.getFormula(formula);
            if(formula == "flat"){
                return this.distanceBetweenFlat(from, to, applicableUnit);
            }else {
                return this.distanceBetweenSphere(from, to, applicableUnit);
            }
        }
    }

    protected LatLng endpoint(LatLng start, Double heading, Double distance, Map<String, String>options){
        Map<String,String> optionMap = getOptions(options);
        Double ratio = distance/EARTH_RADIUS.get(optionMap.get("unit"));
        Double lat     = deg2rad(start.getLat());
        Double lng     = deg2rad(start.getLng());
        heading = deg2rad(heading);
        Double sinRatio = Math.sin(ratio);
        Double cosRatio = Math.cos(ratio);
        Double sinLat = Math.sin(lat);
        Double cosLat = Math.cos(lat);
        Double endLat = Math.asin(sinLat * cosRatio + cosLat * sinRatio * Math.cos(heading));
        Double endLng = lng + Math.atan2(Math.sin(heading) * sinRatio * cosLat,
                cosRatio - sinLat * Math.sin(endLat));
        return new LatLng(rad2deg(endLat), rad2deg(endLng));

    }

    private Double distanceBetweenFlat(LatLng from, LatLng to, String unit){
        Double lat_length = unitsPerLatitudeDegree(unit) * (from.getLat() - to.getLat());
        Double lng_length = unitsPerLongitudeDegree(from.getLat(), unit) * (from.getLng() - to.getLng());
        return Math.sqrt(Math.pow(lat_length,2) + Math.pow(lng_length,2));
    }

    private Double distanceBetweenSphere(LatLng from, LatLng to, String unit){
        try {
            Double latSin = Math.sin(deg2rad(from.getLat())) * Math.sin(deg2rad(to.getLat()));
            Double latCos = Math.cos(deg2rad(from.getLat())) * Math.cos(deg2rad(to.getLat()));
            Double lngCos = Math.cos(deg2rad(to.getLng()) - deg2rad(from.getLng()));
            return EARTH_RADIUS.get(unit) * Math.acos(latSin + latCos * lngCos);
        }catch (Exception ex){
            log.error("Exception {}", ex.getMessage());
            return 0d;
        }
    }


    private Double unitsPerLatitudeDegree(String unit){
        return PER_LATITUDE_DEGREE.get(unit);
    }

    private Double unitsPerLongitudeDegree(Double lat, String unit){
        return EARTH_RADIUS.get(unit) * Math.cos(lat * PI_DIV_RAD) * PI_DIV_RAD;
    }

    private List<Integer> decimalToDms(Double degree){
        Double seconds = ((Math.abs(degree) % 1) * 3600d);
        List<Integer> dmsList = new ArrayList<>();
        Double sec60 = seconds/60;
        Double secMod60 = seconds%60;
        dmsList.add(Math.toIntExact(Math.round(degree)));
        dmsList.add(sec60.intValue());
        dmsList.add(secMod60.intValue());
        return dmsList;
    }


    private Double deg2rad(Double degrees){
        return ((degrees/180.0d) * PI);
    }


    private Double rad2deg(Double rad){
        return (rad*180.0d/PI);
    }

    public Double toHeading(Double rad){
        return (rad2deg(rad) + 360) % 360;
    }

    private Map<String, String> getOptions(Map<String, String > options){
        Map<String, String > optionMap = this.getInitialMap();
        if(options == null){
            return optionMap;
        }
        optionMap.put("unit", this.getUnit(options.get("unit")));
        optionMap.put("formula", this.getFormula(options.get("formula")));
        return optionMap;
    }


    private String getUnit(String unit){
        List<String> supportedUnits = Arrays.asList(SUPPORTED_UNITS);
        if(unit == null){
            return DEFAULT_UNIT;
        }
        if(supportedUnits.contains(unit)){
            return unit;
        }else {
            throw new UnsupportedUnitException(String.format("%s is not a supported Unit", unit));
        }
    }

    private String getFormula(String formula){
        List<String> supportedFormulae = Arrays.asList(SUPPORTED_FORMULAE);
        if(formula == null){
            return DEFAULT_FORMULA;
        }
        if(supportedFormulae.contains(formula)){
            return formula;
        }else {
            throw new UnsupportedUnitException(String.format("%s is not a supported Unit", formula));
        }
    }

    private Map<String, String> getInitialMap(){
        Map<String ,String > optionsMap = new HashMap<>();
        optionsMap.put("unit", DEFAULT_UNIT);
        optionsMap.put("formula", DEFAULT_FORMULA);
        return optionsMap;
    }
}
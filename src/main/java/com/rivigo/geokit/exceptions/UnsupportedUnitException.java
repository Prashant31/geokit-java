package com.rivigo.geokit.exceptions;

/**
 * Created by prashant on 6/25/16.
 *
 * Custom Exception for unsupported Unit
 */
public class UnsupportedUnitException extends RuntimeException {

    public UnsupportedUnitException(String s){
        super(s);
    }
}

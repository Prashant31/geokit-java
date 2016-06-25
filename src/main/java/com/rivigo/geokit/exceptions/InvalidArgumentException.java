package com.rivigo.geokit.exceptions;

/**
 * Created by prashant on 6/25/16.
 *
 * Custom Exception for invalid arguments
 */
public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String s){
        super(s);
    }
}

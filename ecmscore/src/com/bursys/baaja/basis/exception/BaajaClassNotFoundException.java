package com.bursys.baaja.basis.exception;

/**
* 
 * This is a custom Exception for a classnotfound exception .
 * @author JosephBrian
 */
public class BaajaClassNotFoundException extends BaajaException {
    /**
     * no arg constructor
     */
    public BaajaClassNotFoundException() {
    }

    /**
     * String argument constructor
     * @param s
     */
    public BaajaClassNotFoundException(String s) {
        super(s);
    }
}

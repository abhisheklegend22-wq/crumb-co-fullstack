package com.crumbco.bakery.exception;

/** Base domain exception; concrete bakery exceptions inherit common behaviour. */
public class BakeryException extends RuntimeException {
    public BakeryException(String message) { super(message); }
}

package com.crumbco.bakery.exception;

public class InsufficientStockException extends BakeryException {
    public InsufficientStockException(String productName, int requested, int available) {
        super("Insufficient stock for " + productName + ": requested " + requested + ", available " + available);
    }
}

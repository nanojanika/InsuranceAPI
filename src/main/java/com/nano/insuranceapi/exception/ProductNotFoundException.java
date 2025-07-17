package com.nano.insuranceapi.exception;

public class ProductNotFoundException extends RuntimeException {  // to distinguish invalid productId (404) and other input errors (400)
    public ProductNotFoundException(String message) {
        super(message);
    }
}
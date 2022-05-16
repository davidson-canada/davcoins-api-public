package com.davidson.davcoinsapi.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

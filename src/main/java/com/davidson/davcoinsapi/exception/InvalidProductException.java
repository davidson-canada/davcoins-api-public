package com.davidson.davcoinsapi.exception;

public class InvalidProductException extends RuntimeException {

    public InvalidProductException(String errorMessage) {
        super(errorMessage);
    }
}

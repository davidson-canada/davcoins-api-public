package com.davidson.davcoinsapi.exception;

public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String errorMessage) {
        super(errorMessage);
    }
    
}

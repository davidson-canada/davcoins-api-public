package com.davidson.davcoinsapi.exception;

public class UserBalanceException extends RuntimeException {

    public UserBalanceException(String errorMessage) {
        super(errorMessage);
    }
    
}

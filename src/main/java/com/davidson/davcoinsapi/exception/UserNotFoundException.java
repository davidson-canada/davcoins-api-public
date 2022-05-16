package com.davidson.davcoinsapi.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    
}

package com.davidson.davcoinsapi.exception;

public class NotionAPIException extends RuntimeException {

    public NotionAPIException(String errorMessage) {
        super(errorMessage);
    }
    
}

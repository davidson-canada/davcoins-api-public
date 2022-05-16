package com.davidson.davcoinsapi.errors;

import java.time.LocalDateTime;

import com.davidson.davcoinsapi.exception.InvalidProductException;
import com.davidson.davcoinsapi.exception.InvalidTransactionException;
import com.davidson.davcoinsapi.exception.ProductNotFoundException;
import com.davidson.davcoinsapi.exception.NotionAPIException;
import com.davidson.davcoinsapi.exception.UserBalanceException;
import com.davidson.davcoinsapi.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler({ InvalidTransactionException.class, UserBalanceException.class, UserNotFoundException.class,
            ProductNotFoundException.class, InvalidProductException.class, NotionAPIException.class })
    public ResponseEntity<JsonNode> handleException(RuntimeException exception) {

        ObjectNode jsonObject = new ObjectMapper().createObjectNode();

        jsonObject.put("timestamp", LocalDateTime.now().toString());
        jsonObject.put("message", exception.getLocalizedMessage());

        return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
    }

}

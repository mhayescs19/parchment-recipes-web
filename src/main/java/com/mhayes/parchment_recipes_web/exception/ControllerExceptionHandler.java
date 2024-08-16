package com.mhayes.parchment_recipes_web.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Catch malformed JSON objects that does not match the requested body format
     * @param e
     * @return response with the first field that invalidates the payload
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, String> errorResponse = new HashMap<>();

        String invalidField = ((UnrecognizedPropertyException) e.getCause()).getPropertyName();
        errorResponse.put("error", "field does not match payload format");
        errorResponse.put("invalid field", invalidField);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // return 400 bad request
    }

    /**
     * Catch queries with an id that does not map to a valid persisted resource
     * @param e
     * @return response with id and resource requested in the invalid query
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", e.getMessage());
        errorResponse.put("reason", "resource not found"); // exception should be abstracted from the consumer

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // return 404 not found
    }

    @ExceptionHandler(GoogleIdTokenValidationException.class)
    public ResponseEntity<Map<String,String>> handleGoogleIdTokenValidationException(GoogleIdTokenValidationException e) {
        Map<String,String> errorResponse = new HashMap<>();

        errorResponse.put("error",e.getMessage());
        errorResponse.put("reason", "google id validation failed");

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN); // return 401 authorized
    }
}

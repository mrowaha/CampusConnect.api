package com.campusconnect.ui.productTag.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TagExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleTagNotFound(TagNotFoundException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Tag Not Found");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("error", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleTagNotFound(TagAlreadyExistsException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Tag already exists");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("error", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.OK);
    }
}
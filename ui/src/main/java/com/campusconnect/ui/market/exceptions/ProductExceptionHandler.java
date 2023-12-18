package com.campusconnect.ui.market.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(ProductNotFoundException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Product Not Found");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("error", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(ProductNotAvailableException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Product Not Available");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("error", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


}

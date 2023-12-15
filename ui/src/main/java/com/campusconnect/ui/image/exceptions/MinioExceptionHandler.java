package com.campusconnect.ui.image.exceptions;

import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class MinioExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<Map<String, List<String>>> handleInvalidImageType(InvalidFileTypeException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Invalid Image Format. Allowed png, jpeg, jpg");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericMinIOFailureException.class)
    public ResponseEntity<Map<String, List<String>>> handleImageUploadFailure(GenericMinIOFailureException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Image upload failed");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

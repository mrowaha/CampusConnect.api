package com.campusconnect.ui.user.exceptions;

import com.campusconnect.ui.utils.UserUtilities;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleUserNotFound(UserNotFoundException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("User Not Found");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyTakenException.class)
    public ResponseEntity<Map<String, List<String>>> handleUserAlreadyTaken(UserAlreadyTakenException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("User Already Taken");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.SEE_OTHER);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, List<String>>> handleUserAlreadyTaken(InvalidPasswordException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Wrong Password");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(UserSuspendedException.class)
    public ResponseEntity<Map<String, List<String>>> handleUserSuspended(UserSuspendedException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Account Suspended");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserUtilities.AuthToUserException.class)
    public ResponseEntity<Map<String, List<String>>> handleUserFromAuth(UserUtilities.AuthToUserException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Internal Server Error");
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
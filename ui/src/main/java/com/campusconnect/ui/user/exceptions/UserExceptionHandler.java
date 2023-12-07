package com.campusconnect.ui.user.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
<<<<<<<< HEAD:ui/src/main/java/com/campusconnect/ui/user/exceptions/UserExceptionHandler.java
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
========
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {
>>>>>>>> 6e3423623205b3a7f274747b8c047eded8bc39f5:ui/src/main/java/com/campusconnect/ui/user/exceptions/AuthExceptionHandler.java

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

}

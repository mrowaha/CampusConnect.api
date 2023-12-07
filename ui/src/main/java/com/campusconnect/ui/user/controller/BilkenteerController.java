package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.user.dto.ProtectedDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bilkenteer")
public class BilkenteerController {

    @GetMapping(value = "/protected")
    public ResponseEntity<?> protectedBilkenteerRoute() {
        return new ResponseEntity(new ProtectedDto("Authorized"), HttpStatus.OK);
    }

}

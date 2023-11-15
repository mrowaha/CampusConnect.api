package com.campusconnect.api.user.controller;

import com.campusconnect.api.user.dto.BilkenteerCreationDto;
import com.campusconnect.api.user.dto.BilkenteerLoginDto;
import com.campusconnect.api.user.entity.Bilkenteer;
import com.campusconnect.api.user.service.BilkenteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bilkenteer")
@RequiredArgsConstructor
public class BilkenteerController {

    private final BilkenteerService bilkenteerService;

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> registerBilkenteer(
            @RequestBody BilkenteerCreationDto bilkenteerCreationInfo
    ) {
        return bilkenteerService.register(bilkenteerCreationInfo);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> loginBilkenteer(
            @RequestBody BilkenteerLoginDto bilkenteerLoginDto
    ) {
        return bilkenteerService.authenticate(bilkenteerLoginDto);
    }

    @GetMapping(value = "/protected")
    public  ResponseEntity<?> protectedRoute() {
        return new ResponseEntity("Good", HttpStatus.OK);
    }
}

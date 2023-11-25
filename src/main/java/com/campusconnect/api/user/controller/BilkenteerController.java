package com.campusconnect.api.user.controller;

import com.campusconnect.api.user.dto.UserCreationDto;
import com.campusconnect.api.user.dto.UserLoginDto;
import com.campusconnect.api.user.service.BilkenteerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bilkenteer")
@RequiredArgsConstructor
@Controller
public class BilkenteerController {

    private final BilkenteerService bilkenteerService;

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> registerBilkenteer(
           @Valid @RequestBody UserCreationDto bilkenteerCreationInfo
    ) {
        return bilkenteerService.register(bilkenteerCreationInfo);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> loginBilkenteer(
            @Valid @RequestBody UserLoginDto bilkenteerLoginDto
    ) {
        return bilkenteerService.authenticate(bilkenteerLoginDto);
    }

    @GetMapping(value = "/protected")
    public  ResponseEntity<?> protectedRoute() {
        return new ResponseEntity(new ProtectedDto("Authorized"), HttpStatus.OK);
    }
}

@Data
@AllArgsConstructor
class ProtectedDto {
    String success;
}
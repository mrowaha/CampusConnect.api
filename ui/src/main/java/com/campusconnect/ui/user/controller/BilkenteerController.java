package com.campusconnect.ui.user.controller;

import com.campusconnect.email.EmailHandler;
import com.campusconnect.domain.user.dto.UserCreationDto;
import com.campusconnect.domain.user.dto.UserLoginDto;
import com.campusconnect.ui.user.service.BilkenteerService;
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
        EmailHandler.sendEmail("OTP : SMTH404"); // Testing for module compatibility TODO remove this later
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
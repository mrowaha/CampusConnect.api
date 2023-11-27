package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.user.dto.UserCreationDto;
import com.campusconnect.domain.user.dto.UserLoginDto;
import com.campusconnect.ui.user.service.ModeratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moderator")
@RequiredArgsConstructor
@Controller
public class ModeratorController {

    private  final ModeratorService moderatorService;

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> registerBilkenteer(
            @Valid @RequestBody UserCreationDto moderatorCreationInfo
    ) {
        return moderatorService.register(moderatorCreationInfo);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> loginBilkenteer(
            @Valid @RequestBody UserLoginDto moderatorLoginInfo
    ) {
        return moderatorService.authenticate(moderatorLoginInfo);
    }

    @GetMapping(value = "/protected")
    public  ResponseEntity<?> protectedRoute() {
        return new ResponseEntity(new ProtectedDto("Authorized"), HttpStatus.OK);
    }
}

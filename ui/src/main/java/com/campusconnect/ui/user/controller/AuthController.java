package com.campusconnect.ui.user.controller;


import com.campusconnect.domain.user.dto.UserCreationDto;
import com.campusconnect.domain.user.dto.UserLoginDto;

import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.BilkenteerService;
import com.campusconnect.ui.user.service.ModeratorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final BilkenteerService bilkenteerService;
    private final ModeratorService moderatorService;

    @PostMapping(value = "/bilkenteer/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerBilkenteer(
            @Valid @RequestBody UserCreationDto bilkenteerCreationInfo
    ) throws UserAlreadyTakenException {
        return bilkenteerService.register(bilkenteerCreationInfo);
    }

    @PostMapping(value = "/bilkenteer/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> loginBilkenteer(
            @Valid @RequestBody UserLoginDto bilkenteerLoginDto
    ) throws UserNotFoundException, InvalidPasswordException {
        return bilkenteerService.authenticate(bilkenteerLoginDto);
    }


    @PostMapping(value = "/moderator/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerModerator(
            @Valid @RequestBody UserCreationDto moderatorCreationInfo
    ) throws UserAlreadyTakenException {
        return moderatorService.register(moderatorCreationInfo);
    }

    @PostMapping(value = "/moderator/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> loginModerator(
            @Valid @RequestBody UserLoginDto moderatorLoginInfo
    ) throws UsernameNotFoundException, InvalidPasswordException {
        return moderatorService.authenticate(moderatorLoginInfo);
    }

}
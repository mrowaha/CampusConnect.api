package com.campusconnect.ui.user.controller;


import com.campusconnect.domain.user.dto.BearerToken;
import com.campusconnect.domain.user.dto.UserCreationDto;
import com.campusconnect.domain.user.dto.UserLoginDto;

import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.BilkenteerService;
import com.campusconnect.ui.user.service.ModeratorService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends SecureController {

    private static final String BILKENTEER_LOGIN = "/bilkenteer/login";
    private static final String MODERATOR_LOGIN = "/moderator/login";
    private static final String BILKENTEER_REGISTER = "/bilkenteer/register";
    private  static final String MODERATOR_REGISTER = "/moderator/register";

    private final BilkenteerService bilkenteerService;
    private final ModeratorService moderatorService;

    @PostMapping(value = AuthController.BILKENTEER_REGISTER, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BearerToken> registerBilkenteer(
            @Valid @RequestBody UserCreationDto bilkenteerCreationInfo
    ) throws UserAlreadyTakenException {
        return new ResponseEntity<>(
                bilkenteerService.register(bilkenteerCreationInfo),
                HttpStatus.OK
        );
    }

    @PostMapping(value = AuthController.BILKENTEER_LOGIN, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BearerToken> loginBilkenteer(
            @Valid @RequestBody UserLoginDto bilkenteerLoginDto
    ) throws UserNotFoundException, InvalidPasswordException {
        return new ResponseEntity<>(
                bilkenteerService.authenticate(bilkenteerLoginDto),
                HttpStatus.OK
        );
    }


    @PostMapping(value = AuthController.BILKENTEER_REGISTER, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BearerToken> registerModerator(
            @Valid @RequestBody UserCreationDto moderatorCreationInfo
    ) throws UserAlreadyTakenException {
        return new ResponseEntity<>(
                moderatorService.register(moderatorCreationInfo),
                HttpStatus.OK);
    }

    @PostMapping(value = AuthController.MODERATOR_LOGIN, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BearerToken> loginModerator(
            @Valid @RequestBody UserLoginDto moderatorLoginInfo
    ) throws UsernameNotFoundException, InvalidPasswordException {
        return new ResponseEntity<>(
                moderatorService.authenticate(moderatorLoginInfo),
                HttpStatus.OK);
    }

    @Override
    public void postConstruct() {
        if (this.endpoints == null) {
            this.endpoints = new ArrayList<>();
        }
        this.endpoints.add(new Endpoint(HttpMethod.POST, AuthController.BILKENTEER_LOGIN, false));
        this.endpoints.add(new Endpoint(HttpMethod.POST, AuthController.BILKENTEER_REGISTER, false));
        this.endpoints.add(new Endpoint(HttpMethod.POST, AuthController.MODERATOR_REGISTER, false));
        this.endpoints.add(new Endpoint(HttpMethod.POST, AuthController.MODERATOR_LOGIN, false));
    }
}
package com.campusconnect.ui.user.controller;


import com.campusconnect.domain.user.dto.*;

import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.exceptions.UserSuspendedException;
import com.campusconnect.ui.user.service.BilkenteerService;
import com.campusconnect.ui.user.service.ModeratorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends SecureController {

    private static final String BASE_URL = "/auth";
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
    public ResponseEntity<BilkenteerLoginResponse> loginBilkenteer(
            @Valid @RequestBody UserLoginRequestDto bilkenteerLoginDto
    ) throws UserNotFoundException, InvalidPasswordException, UserSuspendedException {
        return new ResponseEntity<>(
                bilkenteerService.authenticate(bilkenteerLoginDto),
                HttpStatus.OK
        );
    }


    @PostMapping(value = AuthController.MODERATOR_REGISTER, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BearerToken> registerModerator(
            @Valid @RequestBody UserCreationDto moderatorCreationInfo
    ) throws UserAlreadyTakenException {
        return new ResponseEntity<>(
                moderatorService.register(moderatorCreationInfo),
                HttpStatus.OK);
    }

    @PostMapping(value = AuthController.MODERATOR_LOGIN, consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserLoginResponseDto> loginModerator(
            @Valid @RequestBody UserLoginRequestDto moderatorLoginInfo
    ) throws UsernameNotFoundException, InvalidPasswordException, UserSuspendedException {
        return new ResponseEntity<>(
                moderatorService.authenticate(moderatorLoginInfo),
                HttpStatus.OK);
    }

    @Override
    public void postConstruct() {
        this.addEndpoint(HttpMethod.POST, AuthController.BASE_URL,AuthController.BILKENTEER_LOGIN, SecurityScope.NONE);
        this.addEndpoint(HttpMethod.POST, AuthController.BASE_URL,AuthController.BILKENTEER_REGISTER, SecurityScope.NONE);
        this.addEndpoint(HttpMethod.POST, AuthController.BASE_URL,AuthController.MODERATOR_REGISTER, SecurityScope.NONE);
        this.addEndpoint(HttpMethod.POST, AuthController.BASE_URL,AuthController.MODERATOR_LOGIN, SecurityScope.NONE);
    }
}
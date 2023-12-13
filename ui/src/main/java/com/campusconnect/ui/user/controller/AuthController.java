package com.campusconnect.ui.user.controller;


import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.security.dto.BearerToken;
import com.campusconnect.domain.user.dto.*;

import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.utils.JwtUtilities;
import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.exceptions.UserSuspendedException;
import com.campusconnect.ui.user.service.BilkenteerService;
import com.campusconnect.ui.user.service.ModeratorService;

import com.campusconnect.ui.utils.UserUtilities;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends SecureController {

    private final BilkenteerService bilkenteerService;
    private final ModeratorService moderatorService;
    private final UserUtilities userUtilities;

    @PostMapping(value = "/bilkenteer/register", consumes = "application/json", produces = "application/json")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<BearerToken> registerBilkenteer(
            @Valid @RequestBody UserCreationDto bilkenteerCreationInfo
    ) throws UserAlreadyTakenException {
        return new ResponseEntity<>(
                bilkenteerService.register(bilkenteerCreationInfo),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/bilkenteer/login", consumes = "application/json", produces = "application/json")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<BilkenteerLoginResponse> loginBilkenteer(
            @Valid @RequestBody UserLoginRequestDto bilkenteerLoginDto
    ) throws UserNotFoundException, InvalidPasswordException, UserSuspendedException {
        return new ResponseEntity<>(
                bilkenteerService.authenticate(bilkenteerLoginDto),
                HttpStatus.OK
        );
    }


    @PostMapping(value = "/moderator/register", consumes = "application/json", produces = "application/json")
    @RequiredScope(scope = SecurityScope.ADMIN)
    public ResponseEntity<BearerToken> registerModerator(
            @RequestHeader("x-api-key") String apikey,
            @Valid @RequestBody UserCreationDto moderatorCreationInfo
    ) throws UserAlreadyTakenException {
        return new ResponseEntity<>(
                moderatorService.register(moderatorCreationInfo),
                HttpStatus.OK);
    }

    @PostMapping(value = "/moderator/login", consumes = "application/json", produces = "application/json")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<ModeratorLoginResponseDto> loginModerator(
            @Valid @RequestBody UserLoginRequestDto moderatorLoginInfo
    ) throws UsernameNotFoundException, InvalidPasswordException, UserSuspendedException {
        return new ResponseEntity<>(
                moderatorService.authenticate(moderatorLoginInfo),
                HttpStatus.OK);
    }

    @GetMapping
    @RequiredScope(scope = SecurityScope.SHARED)
    public ResponseEntity<?> validateToken(
            Authentication authentication
    )  throws UserUtilities.AuthToUserException {
        User user = userUtilities.getUserFromAuth(authentication);
        return switch(user.getRole()) {
            case BILKENTEER -> {
                yield ResponseEntity.ok(
                        BilkenteerLoginResponse.builder()
                                .uuid(user.getUserId())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .role(user.getRole())
                                .trustScore(((Bilkenteer) user).getTrustScore())
                                .build()
                );
            }
            case MODERATOR -> {
                yield ResponseEntity.ok(
                        ModeratorLoginResponseDto.builder()
                                .uuid(user.getUserId())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .role(user.getRole())
                                .build()
                );
            }
        };
    }
}
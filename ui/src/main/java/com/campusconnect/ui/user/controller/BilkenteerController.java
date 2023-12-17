package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.admin.dto.UserSuspendRequestDto;
import com.campusconnect.domain.admin.dto.UserSuspendResponseDto;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.user.dto.BilkenteerContactInfoDto;
import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.domain.user.dto.UserInfoDto;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.BilkenteerService;
import com.campusconnect.ui.utils.UserUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import com.campusconnect.ui.productTag.exceptions.TagNotFoundException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.BilkenteerService;
import com.campusconnect.ui.utils.UserUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bilkenteer")
public class BilkenteerController extends SecureController {

    private final BilkenteerService bilkenteerService;
    private final UserUtilities userUtilities;

    @Autowired
    public BilkenteerController(
            BilkenteerService bilkenteerService,
            UserUtilities userUtilities) {
        this.bilkenteerService = bilkenteerService;
        this.userUtilities = userUtilities;
    }

    @PostMapping(value = "/contact", consumes = "application/json")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<String> updateContactInfo (
            Authentication authentication,
            @Valid @RequestBody BilkenteerContactInfoDto contactInfoDto
    ) throws UserUtilities.AuthToUserException {
        User user = userUtilities.getUserFromAuth(authentication);
        bilkenteerService.addContactInfo(user.getUserId(), contactInfoDto);
        return ResponseEntity.ok("updated your contact info");
    }

    @GetMapping("/all")
    @RequiredScope(scope = SecurityScope.MODERATOR)
    public ResponseEntity<List<UserInfoDto>> listAll() {
        return ResponseEntity.ok(bilkenteerService.listAll());
    }

    @PutMapping(value = "/suspend", consumes = "application/json", produces = "application/json")
    @RequiredScope(scope = SecurityScope.MODERATOR)
    public ResponseEntity<UserSuspendResponseDto> suspendBilkenteer(
            @Valid @RequestBody UserSuspendRequestDto suspendRequestDto
    ) throws UserNotFoundException {
        return ResponseEntity.ok(bilkenteerService.suspend(suspendRequestDto));
    }

    @PutMapping(value = "/unsuspend", consumes = "application/json", produces = "application/json")
    @RequiredScope(scope = SecurityScope.MODERATOR)
    public ResponseEntity<UserSuspendResponseDto> unsuspendBilkenteer(
        @Valid @RequestBody UserSuspendRequestDto unsuspendRequestDto
    ) throws UserNotFoundException {
        return ResponseEntity.ok(bilkenteerService.unsuspend(unsuspendRequestDto));
    }


    private final BilkenteerService bilkenteerService;
    private final UserUtilities userUtilities;

    @GetMapping(value = "/s")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<ProtectedDto> protectedBilkenteerRoute() {
        System.out.println("in bilkenteer protected");
        return new ResponseEntity<>(new ProtectedDto("Authorized"), HttpStatus.OK);
    }

    @GetMapping(value = "/{hello}")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<String> test(
            @PathVariable("hello") String hello
    ) {
        return ResponseEntity.ok(String.format("value: %s", hello));
    }

    @PostMapping("/subscribeToTag")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<ProtectedDto> subscribeToTag(
                Authentication authentication,
                @RequestParam String tagName
    ) throws UserUtilities.AuthToUserException {
            User user = userUtilities.getUserFromAuth(authentication);
            bilkenteerService.subscribeToTag(user.getUserId(), tagName);
            return new ResponseEntity<>(new ProtectedDto("Subscribed"), HttpStatus.OK);
    }

    @PostMapping("/unsubscribeFromTag")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<ProtectedDto> unsubscribeFromTag(
            Authentication authentication,
            @RequestParam String tagName
    ) throws UserUtilities.AuthToUserException {
        User user = userUtilities.getUserFromAuth(authentication);
        bilkenteerService.unsubscribeFromTag(user.getUserId(), tagName);
        return new ResponseEntity<>(new ProtectedDto("Unsubscribed"), HttpStatus.OK);
    }

}


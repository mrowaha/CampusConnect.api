package com.campusconnect.ui.admin.controller;

import com.campusconnect.domain.admin.dto.UserSuspendRequestDto;
import com.campusconnect.domain.admin.dto.UserSuspendResponseDto;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.domain.user.dto.UserCreationDto;
import com.campusconnect.domain.user.dto.UserInfoDto;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;

import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.ModeratorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController extends SecureController {

    private final ModeratorService moderatorService;

    @GetMapping
    @RequiredScope(scope = SecurityScope.ADMIN)
    public ResponseEntity<ProtectedDto> validateApiKey() {
        return ResponseEntity.ok(new ProtectedDto("admin authorized"));
    }

    @PostMapping(value = "/moderator/register", consumes = "application/json", produces = "application/json")
    @RequiredScope(scope = SecurityScope.ADMIN)
    public ResponseEntity<?> registerModerator(
            @Valid @RequestBody UserCreationDto moderatorCreationInfo
    ) throws UserAlreadyTakenException {
        moderatorService.register(moderatorCreationInfo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/moderators")
    @RequiredScope(scope = SecurityScope.ADMIN)
    public ResponseEntity<List<UserInfoDto>> getAllModerators() {
        return ResponseEntity.ok(moderatorService.listAll());
    }

    @PutMapping(value= "/moderator/suspend", produces = "application/json")
    @RequiredScope(scope = SecurityScope.ADMIN)
    public ResponseEntity<UserSuspendResponseDto> suspendModerator(
            @Valid @RequestBody UserSuspendRequestDto suspendRequestDto
    )   throws UserNotFoundException {
        return ResponseEntity.ok(moderatorService.suspend(suspendRequestDto));
    }

    @PutMapping(value= "/moderator/unsuspend", produces = "application/json")
    @RequiredScope(scope = SecurityScope.ADMIN)
    public ResponseEntity<UserSuspendResponseDto> unsuspendModerator(
        @Valid @RequestBody UserSuspendRequestDto unsuspendRequestDto /* intutively, this api will use the same dto to unsuspend */
    ) throws UserNotFoundException {
        return ResponseEntity.ok(moderatorService.unsuspend(unsuspendRequestDto));
    }


}

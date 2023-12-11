package com.campusconnect.ui.admin.controller;

import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.ui.common.controller.SecureController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController extends SecureController {

    @GetMapping
    @RequiredScope(scope = SecurityScope.ADMIN)
    public ResponseEntity<ProtectedDto> validateApiKey() {
        return ResponseEntity.ok(new ProtectedDto("admin authorized"));
    }

}

package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.user.dto.ProtectedDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
@RequestMapping("/moderator")
public class ModeratorController {

    @GetMapping(value = "/s")
    public ResponseEntity<ProtectedDto> protectedModeratorRoute() {
        return new ResponseEntity<>(new ProtectedDto("Authorized"), HttpStatus.OK);
    }

}

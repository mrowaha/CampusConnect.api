package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.ui.common.controller.SecureController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;

@RestController
@RequestMapping("/moderator")
public class ModeratorController extends SecureController {

    private static final String BASE_URL = "/moderator";
    @GetMapping(value = "/s")
    public ResponseEntity<ProtectedDto> protectedModeratorRoute() {
        return new ResponseEntity<>(new ProtectedDto("Authorized"), HttpStatus.OK);
    }

    @Override
    public void postConstruct() {
        this.addEndpoint(HttpMethod.GET, BASE_URL,"/s", SecurityScope.MODERATOR);
    }
}

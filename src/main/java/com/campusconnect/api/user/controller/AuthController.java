package com.campusconnect.api.user.controller;

import com.campusconnect.api.user.dto.UserCreationDto;
import com.campusconnect.api.user.dto.UserLoginDto;
import com.campusconnect.api.user.service.BilkenteerService;
import com.campusconnect.api.user.service.ModeratorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Data
@AllArgsConstructor
class ProtectedDto {
    String success;
}
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final BilkenteerService bilkenteerService;
    private final ModeratorService moderatorService;

    @PostMapping(value = "/bilkenteer/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerBilkenteer(
            @Valid @RequestBody UserCreationDto bilkenteerCreationInfo
    ) {
        return bilkenteerService.register(bilkenteerCreationInfo);
    }

    @PostMapping(value = "/bilkenteer/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> loginBilkenteer(
            @Valid @RequestBody UserLoginDto bilkenteerLoginDto
    ) {
        return bilkenteerService.authenticate(bilkenteerLoginDto);
    }

    @GetMapping(value = "/bilkenteer/protected")
    public  ResponseEntity<?> protectedBilkenteerRoute() {
        return new ResponseEntity(new ProtectedDto("Authorized"), HttpStatus.OK);
    }

    @PostMapping(value = "/moderator/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerModerator(
            @Valid @RequestBody UserCreationDto moderatorCreationInfo
    ) {
        return moderatorService.register(moderatorCreationInfo);
    }

    @PostMapping(value = "/moderator/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> loginModerator(
            @Valid @RequestBody UserLoginDto moderatorLoginInfo
    ) {
        return moderatorService.authenticate(moderatorLoginInfo);
    }

    @GetMapping(value = "/moderator/protected")
    public  ResponseEntity<?> protectedModeratorRoute() {
        return new ResponseEntity(new ProtectedDto("Authorized"), HttpStatus.OK);
    }

}

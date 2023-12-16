package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.common.controller.SecureController;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bilkenteer")
public class BilkenteerController extends SecureController {

    private final BilkenteerService bilkenteerService;
    private final UserUtilities userUtilities;

    @GetMapping(value = "/s")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<ProtectedDto> protectedBilkenteerRoute() {
        System.out.println("in bilkenteer protected");
        return new ResponseEntity<>(new ProtectedDto("Authorized"), HttpStatus.OK);
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


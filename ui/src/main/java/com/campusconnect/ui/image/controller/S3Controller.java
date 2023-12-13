package com.campusconnect.ui.image.controller;

import cn.hutool.core.lang.Pair;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.utils.JwtUtilities;
import com.campusconnect.ui.image.service.ProfileS3Service;
import com.campusconnect.ui.utils.UserUtilities;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/s3")
public class S3Controller extends SecureController {

    private final ProfileS3Service profileS3Service;

    private final UserUtilities userUtilities;

    @Autowired
    public S3Controller(
            ProfileS3Service profileS3Service,
            UserUtilities userUtilities
            ) {
        this.profileS3Service = profileS3Service;
        this.userUtilities = userUtilities;
    }

    @PostMapping(value = "/profile-picture")
    @RequiredScope(scope = SecurityScope.SHARED)
    public ResponseEntity<FileResponse> uploadProfilePicture(
            Authentication authentication,
            @RequestPart(value = "file") MultipartFile imageFile
            )
            throws InvalidFileTypeException,
            UserUtilities.AuthToUserException,
            GenericMinIOFailureException
    {
        User user = userUtilities.getUserFromAuth(authentication);
        return new ResponseEntity<>(
            this.profileS3Service.upload(
                    imageFile,
                    user.getEmail(),
                    user.getRole()
                ), HttpStatus.OK);
    }

    @GetMapping(value = "/profile-picture")
    @RequiredScope(scope = SecurityScope.NONE)
    public void getProfilePicture(
            HttpServletResponse response,
            @RequestParam("userid") UUID userid,
            @RequestParam("role") Role role
            ) throws IOException {
        this.profileS3Service.get
                (response, userid, role);
    }

}

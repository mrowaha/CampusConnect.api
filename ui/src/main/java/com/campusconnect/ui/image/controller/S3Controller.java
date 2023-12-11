package com.campusconnect.ui.image.controller;

import cn.hutool.core.lang.Pair;
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

    private static final String BASE_URL = "/s3";

    private static final String PROFILE_URL = "/profile-picture";

    private final JwtUtilities jwtUtilities;
    private final ProfileS3Service profileS3Service;

    private final UserUtilities userUtilities;

    @Autowired
    public S3Controller(
            ProfileS3Service profileS3Service,
            JwtUtilities jwtUtilities,
            UserUtilities userUtilities
            ) {
        this.profileS3Service = profileS3Service;
        this.jwtUtilities = jwtUtilities;
        this.userUtilities = userUtilities;
    }

    @PostMapping(value = PROFILE_URL)
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

    @GetMapping(value = PROFILE_URL)
    public void getProfilePicture(
            HttpServletResponse response,
            @RequestParam("userid") UUID userid,
            @RequestParam("role") Role role
            ) throws IOException {
        this.profileS3Service.get
                (response, userid, role);
    }

    @Override
    public void postConstruct() {
        this.addEndpoint(HttpMethod.POST, BASE_URL, PROFILE_URL, SecurityScope.SHARED);
        this.addEndpoint(HttpMethod.GET, BASE_URL, PROFILE_URL,  SecurityScope.NONE);
    }
}

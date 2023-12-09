package com.campusconnect.ui.image.controller;

import com.campusconnect.domain.image.dto.ProfilePictureRequestDto;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.config.JwtUtilities;
import com.campusconnect.ui.image.service.ProfileS3Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
public class S3Controller extends SecureController {

    private static final String BASE_URL = "/s3";

    private static final String PROFILE_URL = "/profile-picture";

    private final JwtUtilities jwtUtilities;
    private final ProfileS3Service profileS3Service;

    @Autowired
    public S3Controller(
            ProfileS3Service profileS3Service,
            JwtUtilities jwtUtilities
            ) {
        this.profileS3Service = profileS3Service;
        this.jwtUtilities = jwtUtilities;
    }

    @PostMapping(value = PROFILE_URL)
    public ResponseEntity<FileResponse> uploadProfilePicture(
            @RequestPart(value = "file") MultipartFile imageFile,
            @RequestHeader(name="Authorization") String bearerToken
            )
            throws InvalidFileTypeException,
            GenericMinIOFailureException
    {
        String token = jwtUtilities.getToken(bearerToken);
        String email = jwtUtilities.extractUsername(token);
        Role role = jwtUtilities.extractRole(token);
        return new ResponseEntity<>(
            this.profileS3Service.upload(
                    imageFile,
                    email,
                    role
                ), HttpStatus.OK);
    }

    @GetMapping(value = PROFILE_URL)
    public void getProfilePicture(
            HttpServletResponse response,
            @Valid @RequestBody ProfilePictureRequestDto profilePictureRequestDto
            ) throws IOException {
        this.profileS3Service.get
                (response, profilePictureRequestDto.getUuid(), profilePictureRequestDto.getRole());
    }

    @Override
    public void postConstruct() {
        this.addEndpoint(HttpMethod.POST, BASE_URL, PROFILE_URL, SecurityScope.SHARED);
        this.addEndpoint(HttpMethod.GET, BASE_URL, PROFILE_URL,  SecurityScope.NONE);
    }
}

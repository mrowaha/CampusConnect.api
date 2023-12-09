package com.campusconnect.ui.image.controller;

import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import com.campusconnect.ui.config.JwtUtilities;
import com.campusconnect.ui.image.service.ProfileS3Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
public class S3Controller {

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

    @GetMapping
    public ResponseEntity<ProtectedDto> sayhello() {
        return new ResponseEntity<>(new ProtectedDto("hello"), HttpStatus.OK);
    }

    @PostMapping(value = "/profile-picture")
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

    @GetMapping(value = "/profile-picture")
    public void getProfilePicture(
            HttpServletResponse response,
            @RequestHeader(name = "Authorization") String bearerToken
    ) throws IOException {
        String token = jwtUtilities.getToken(bearerToken);
        String email = jwtUtilities.extractUsername(token);
        Role role = jwtUtilities.extractRole(token);
        this.profileS3Service.get
                (response, email, role);
    }
}

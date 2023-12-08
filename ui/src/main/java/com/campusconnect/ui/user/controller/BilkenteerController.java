package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.image.config.MinioConfigProperties;
import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import com.campusconnect.image.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bilkenteer")
public class BilkenteerController {

    private final MinioConfigProperties minioConfigProperties;
    private final MinioService minioService;

    @Autowired
    public BilkenteerController(MinioConfigProperties minioConfigProperties, MinioService minioService) {
        this.minioConfigProperties = minioConfigProperties;
        this.minioService = minioService;
    }

    @GetMapping(value = "/protected")
    public ResponseEntity<ProtectedDto> protectedBilkenteerRoute() {
        return new ResponseEntity<>(new ProtectedDto("Authorized"), HttpStatus.OK);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestPart("file") MultipartFile imageFile
    )
        throws InvalidFileTypeException, GenericMinIOFailureException
    {
        System.out.println(imageFile);
        try {
            String profilePictureBucket = minioConfigProperties.getBucketName();
            FileResponse uploadResponse = minioService.putObject(
                    imageFile,
                    profilePictureBucket
            );
            return new ResponseEntity<>(uploadResponse, HttpStatus.OK);
        } catch (Exception e) {
            if (e instanceof InvalidFileTypeException) {
                throw  (InvalidFileTypeException) e;
            } else {
                throw new GenericMinIOFailureException();
            }
        }
    }

}

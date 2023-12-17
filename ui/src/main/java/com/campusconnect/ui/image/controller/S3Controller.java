package com.campusconnect.ui.image.controller;

import cn.hutool.core.lang.Pair;
import com.campusconnect.domain.product.dto.ProductIdDto;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.image.exceptions.UnAuthorizedImageUpload;
import com.campusconnect.ui.image.service.ProductS3Service;
import com.campusconnect.ui.market.exceptions.ProductNotFoundException;
import com.campusconnect.ui.utils.JwtUtilities;
import com.campusconnect.ui.image.service.ProfileS3Service;
import com.campusconnect.ui.utils.UserUtilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller extends SecureController {

    private final ProfileS3Service profileS3Service;

    private final UserUtilities userUtilities;

    private final ProductS3Service productS3Service;

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

    @PostMapping(value = "/product-picture")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<List<FileResponse>> uploadBatchProductPicture(
            Authentication authentication,
            @RequestParam Map<String, String> allParams, HttpServletRequest request
    )
        throws UserUtilities.AuthToUserException, GenericMinIOFailureException
                        , InvalidFileTypeException, ProductNotFoundException, UnAuthorizedImageUpload
    {
        if (!allParams.containsKey("productId")) throw new GenericMinIOFailureException();
        ProductIdDto productIdDto = null;
        try {
            productIdDto = new ObjectMapper().readValue(allParams.get("productId"), ProductIdDto.class);
        } catch (JsonProcessingException e) {
            throw new GenericMinIOFailureException();
        }

        User user = userUtilities.getUserFromAuth(authentication);
        Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
        if (request instanceof MultipartHttpServletRequest multiRequest) {
            fileMap = multiRequest.getFileMap();
        }
        return ResponseEntity.ok(
                productS3Service.uploadBatch(
                        fileMap,
                        user.getEmail(),
                        productIdDto.getId()
                )
        );
    }


    @PostMapping(value = "/product-picture/single")
    @RequiredScope(scope = SecurityScope.SHARED)
    public ResponseEntity<FileResponse> uploadProductPicture(
            Authentication authentication,
            @RequestPart(value = "file") MultipartFile imageFile,
            @RequestPart(value = "productId") ProductIdDto productId
    )
            throws UserUtilities.AuthToUserException, GenericMinIOFailureException
            , InvalidFileTypeException, ProductNotFoundException, UnAuthorizedImageUpload
    {
        User user = userUtilities.getUserFromAuth(authentication);
        return new ResponseEntity<>(
                this.productS3Service.uploadSingle(
                        imageFile,
                        user.getEmail(),
                        productId.getId()
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

    @GetMapping(value = "/product-pictures/{id}")
    @RequiredScope(scope = SecurityScope.NONE)
    public void getProductPictures(
            HttpServletResponse response,
            @PathVariable("id") UUID id
    ) throws IOException {
        this.productS3Service.get
                (response, id);
    }

}

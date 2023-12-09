package com.campusconnect.ui.image.service;

import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.domain.user.repository.ModeratorRepository;
import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import com.campusconnect.image.service.MinioService;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileS3Service {

    private final BilkenteerRepository bilkenteerRepository;
    private final ModeratorRepository moderatorRepository;
    private final MinioService minioService;


    public interface  SaltedObjectName {
        String saltedObjectName(final String email, final Role role) throws GenericMinIOFailureException;
    }
    public static String saltObjectName(SaltedObjectName saltedObjectName, String email, Role role) throws GenericMinIOFailureException {
        return saltedObjectName.saltedObjectName(email, role);
    }


    public FileResponse upload(MultipartFile imageFile, String email, Role role)
        throws InvalidFileTypeException, GenericMinIOFailureException
    {
        String saltedName = saltObjectName((email1, role1) -> {
            String base = email1 + "-" + role1.toString();
            final MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new GenericMinIOFailureException();
            }
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }, email, role);

        log.info("Salted Name for email {} and role {} is {}", email, role.toString(), saltedName);
        String fileName = imageFile.getOriginalFilename();
        String objectName = saltedName + fileName.substring(fileName.lastIndexOf("."));

        int updatedRows = 0;
        switch (role) {
            case BILKENTEER -> {
                updatedRows = bilkenteerRepository.updateProfilePictureByEmail(email, objectName);
            }
            case MODERATOR -> {
                updatedRows = moderatorRepository.updateProfilePictureByEmail(email, objectName);
            }
        }

        if (updatedRows != 1) {
            // we need the updatedRows to be exactly one
            // the controller must ensure that the email exists
            // one way to ensure it is to use the JWT Token
            // and extract role and email claims
            log.error("Could Not Update Profile Picture for user {}", email);
            throw new GenericMinIOFailureException();
        }

        try {
            return minioService.putProfilePicture(imageFile, saltedName);
        } catch (InvalidFileTypeException | GenericMinIOFailureException exception) {
            switch (role) {
                case BILKENTEER -> {
                    bilkenteerRepository.updateProfilePictureByEmail(email, null);
                }
                case MODERATOR -> {
                    moderatorRepository.updateProfilePictureByEmail(email, null);
                }
            }
            throw exception;
        }
    }

    public void get(HttpServletResponse response, UUID userId, Role role)
        throws GenericMinIOFailureException, UserNotFoundException, IOException
    {
        String profilePictureName = null;
        User user = null;
        switch (role) {
            case BILKENTEER -> {
                user = bilkenteerRepository.findById(userId)
                        .orElseThrow(UserNotFoundException::new);
            }
            case MODERATOR -> {
                user = moderatorRepository.findById(userId)
                        .orElseThrow(UserNotFoundException::new);
            }
        }
        if (user == null) return;
        profilePictureName = user.getProfilePicture();
        if (profilePictureName == null) return;

        InputStream in = null;
        try {
            in = minioService.getProfilePicture(profilePictureName);
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(profilePictureName, StandardCharsets.UTF_8));
            response.setCharacterEncoding("UTF-8");
            // Remove bytes from InputStream Copied to the OutputStream .
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            throw new GenericMinIOFailureException();
        } finally {
            if (in != null) in.close();
        }
    }
}

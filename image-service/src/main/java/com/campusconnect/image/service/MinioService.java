package com.campusconnect.image.service;

import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {

    // Upload profile picture in the bucket
    FileResponse putProfilePicture(MultipartFile multipartFile, String objectName) throws GenericMinIOFailureException, InvalidFileTypeException;


    // Download file from bucket
    InputStream getProfilePicture(String objectName) throws GenericMinIOFailureException, InvalidFileTypeException;


    FileResponse putProductPicture(MultipartFile multipartFile, String objectName) throws GenericMinIOFailureException, InvalidFileTypeException;


    // Download file from bucket
    InputStream getProductPicture(String objectName) throws GenericMinIOFailureException, InvalidFileTypeException;

    FileResponse putForumPicture(MultipartFile multipartFile, String objectName) throws GenericMinIOFailureException, InvalidFileTypeException;


    // Download file from bucket
    InputStream getForumPicture(String objectName) throws GenericMinIOFailureException, InvalidFileTypeException;
}
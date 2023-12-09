package com.campusconnect.image.service.impl;

import com.campusconnect.image.config.MinioConfigProperties;
import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import com.campusconnect.image.service.MinioService;
import com.campusconnect.image.util.ImageTypeUtils;
import com.campusconnect.image.util.MinioUtil;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MinioServiceImpl implements MinioService {

    private final ImageTypeUtils imageTypeUtils;
    private final MinioUtil minioUtil;

    private final MinioConfigProperties properties;

    @Autowired
    public MinioServiceImpl(
            ImageTypeUtils imageTypeUtils,
            MinioUtil minioUtil,
            MinioConfigProperties properties) {
        this.minioUtil = minioUtil;
        this.imageTypeUtils = imageTypeUtils;
        this.properties = properties;
    }

    private FileResponse putObject(MultipartFile multipartFile, String bucketName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, InvalidFileTypeException {
        // validate incoming image type

        String fileType = imageTypeUtils.getImageType(multipartFile);
        byte[] fileData = multipartFile.getBytes();
        String fileName = multipartFile.getOriginalFilename();
        Long fileSize = multipartFile.getSize();
        LocalDateTime createdTime = LocalDateTime.now();
        String objectName = UUID.randomUUID().toString().replaceAll("-", "")
                + fileName.substring(fileName.lastIndexOf("."));
        minioUtil.putObject(bucketName, fileData, objectName,fileType);

        return FileResponse.builder()
                .fileName(objectName)
                .fileSize(fileSize)
                .contentType(fileType)
                .createdTime(createdTime)
                .build();
    }

    private FileResponse putObject(MultipartFile multipartFile, String bucketName, String objectName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, InvalidFileTypeException {
        String fileType = imageTypeUtils.getImageType(multipartFile);
        String fileName = multipartFile.getOriginalFilename();
        byte[] fileData = multipartFile.getBytes();
        Long fileSize = multipartFile.getSize();
        LocalDateTime createdTime = LocalDateTime.now();

        objectName = objectName + fileName.substring(fileName.lastIndexOf("."));
        minioUtil.putObject(bucketName, fileData, objectName,fileType);
        return FileResponse.builder()
                .fileName(objectName)
                .fileSize(fileSize)
                .contentType(fileType)
                .createdTime(createdTime)
                .build();
    }

    @Override
    public FileResponse putProfilePicture(MultipartFile multipartFile, String objectName) throws InvalidFileTypeException, GenericMinIOFailureException {
        String profilePictureBucket = properties.getProfileBucketName();
        try {
            return this.putObject(
                    multipartFile,
                    profilePictureBucket,
                    objectName
            );
        } catch (Exception e) {
            if (e instanceof  InvalidFileTypeException) {
                throw (InvalidFileTypeException)  e;
            } else {
                throw new GenericMinIOFailureException();
            }
        }
    }

    @Override
    public InputStream getProfilePicture(String objectName) throws GenericMinIOFailureException, InvalidFileTypeException {
        String profilePictureBucket = properties.getProfileBucketName();
        try {
            InputStream is = this.downloadObject(profilePictureBucket, objectName);
            if (is == null) {
                throw new GenericMinIOFailureException();
            }
            return is;
        } catch (Exception e) {
            throw new GenericMinIOFailureException();
        }
    }

    public InputStream downloadObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioUtil.getObject(bucketName, objectName);
    }
}

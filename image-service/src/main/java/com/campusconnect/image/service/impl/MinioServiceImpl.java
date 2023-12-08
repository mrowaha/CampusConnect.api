package com.campusconnect.image.service.impl;

import com.campusconnect.image.dto.FileResponse;
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

    @Autowired
    public MinioServiceImpl(ImageTypeUtils imageTypeUtils, MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
        this.imageTypeUtils = imageTypeUtils;
    }

    @Override
    public FileResponse putObject(MultipartFile multipartFile, String bucketName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, InvalidFileTypeException {
        // validate incoming image type

        String fileType = imageTypeUtils.getImageType(multipartFile);
        System.out.println("file type is " + fileType);
        byte[] filedata = multipartFile.getBytes();
        String fileName = multipartFile.getOriginalFilename();
        Long fileSize = multipartFile.getSize();
        LocalDateTime createdTime = LocalDateTime.now();
        String objectName = UUID.randomUUID().toString().replaceAll("-", "")
                + fileName.substring(fileName.lastIndexOf("."));
        minioUtil.putObject(bucketName, filedata, objectName,fileType);

        return FileResponse.builder()
                .filename(objectName)
                .fileSize(fileSize)
                .contentType(fileType)
                .createdTime(createdTime)
                .build();
    }

    @Override
    public InputStream downloadObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioUtil.getObject(bucketName, objectName);
    }
}

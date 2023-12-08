package com.campusconnect.image.service;

import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MinioService {

    // Upload files in the bucket
    FileResponse putObject(MultipartFile multipartFile, String bucketName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, InvalidFileTypeException;

    // Download file from bucket
    InputStream downloadObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}

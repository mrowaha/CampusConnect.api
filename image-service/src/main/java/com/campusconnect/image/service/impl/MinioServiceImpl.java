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

/**
 * Implementation of the MinioService interface for managing objects in MinIO storage.
 */
@Service
public class MinioServiceImpl implements MinioService {

    private final ImageTypeUtils imageTypeUtils;
    private final MinioUtil minioUtil;

    private final MinioConfigProperties properties;

    /**
     * Constructor for MinioServiceImpl.
     *
     * @param imageTypeUtils Utility for determining the image type.
     * @param minioUtil      Utility for interacting with MinIO.
     * @param properties     Configuration properties for MinIO.
     */
    @Autowired
    public MinioServiceImpl(
            ImageTypeUtils imageTypeUtils,
            MinioUtil minioUtil,
            MinioConfigProperties properties) {
        this.minioUtil = minioUtil;
        this.imageTypeUtils = imageTypeUtils;
        this.properties = properties;
    }

    /**
     * Uploads an object to the specified MinIO bucket with a generated object name.
     *
     * @param multipartFile The file to upload.
     * @param bucketName    The MinIO bucket name.
     * @return A FileResponse containing information about the uploaded file.
     * @throws IOException               If an I/O error occurs.
     * @throws ServerException           If an error occurs on the server.
     * @throws InsufficientDataException If there is insufficient data.
     * @throws ErrorResponseException    If an error response is received.
     * @throws NoSuchAlgorithmException  If a required cryptographic algorithm is not available.
     * @throws InvalidKeyException       If the key is invalid.
     * @throws InvalidResponseException   If the response is invalid.
     * @throws XmlParserException        If an XML parsing error occurs.
     * @throws InternalException         If an internal error occurs.
     * @throws InvalidFileTypeException  If the file type is not valid.
     */
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

    /**
     * Uploads an object to the specified MinIO bucket with a custom object name.
     *
     * @param multipartFile The file to upload.
     * @param bucketName    The MinIO bucket name.
     * @param objectName    The custom object name.
     * @return A FileResponse containing information about the uploaded file.
     * @throws IOException               If an I/O error occurs.
     * @throws ServerException           If an error occurs on the server.
     * @throws InsufficientDataException If there is insufficient data.
     * @throws ErrorResponseException    If an error response is received.
     * @throws NoSuchAlgorithmException  If a required cryptographic algorithm is not available.
     * @throws InvalidKeyException       If the key is invalid.
     * @throws InvalidResponseException   If the response is invalid.
     * @throws XmlParserException        If an XML parsing error occurs.
     * @throws InternalException         If an internal error occurs.
     * @throws InvalidFileTypeException  If the file type is not valid.
     */
    private FileResponse putObject(MultipartFile multipartFile, String bucketName, String objectName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, InvalidFileTypeException {
        String fileType = imageTypeUtils.getImageType(multipartFile);
        byte[] fileData = multipartFile.getBytes();
        Long fileSize = multipartFile.getSize();
        LocalDateTime createdTime = LocalDateTime.now();
        minioUtil.putObject(bucketName, fileData, objectName,fileType);
        return FileResponse.builder()
                .fileName(objectName)
                .fileSize(fileSize)
                .contentType(fileType)
                .createdTime(createdTime)
                .build();
    }

    /**
     * Uploads a profile picture to the MinIO bucket with a specified object name.
     *
     * @param multipartFile The profile picture file to upload.
     * @param objectName    The object name for the profile picture.
     * @return A FileResponse containing information about the uploaded profile picture.
     * @throws InvalidFileTypeException   If the file type is not valid.
     * @throws GenericMinIOFailureException If a generic MinIO failure occurs.
     */
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

    /**
     * Retrieves a profile picture from the MinIO bucket with the specified object name.
     *
     * @param objectName The object name of the profile picture.
     * @return An InputStream containing the profile picture data.
     * @throws GenericMinIOFailureException If a generic MinIO failure occurs.
     * @throws InvalidFileTypeException     If the file type is not valid.
     */
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

    /**
     * Downloads an object from the specified MinIO bucket.
     *
     * @param bucketName The MinIO bucket name.
     * @param objectName The object name of the file to download.
     * @return An InputStream containing the file data.
     * @throws ServerException           If an error occurs on the server.
     * @throws InsufficientDataException If there is insufficient data.
     * @throws ErrorResponseException    If an error response is received.
     * @throws IOException               If an I/O error occurs.
     * @throws NoSuchAlgorithmException  If a required cryptographic algorithm is not available.
     * @throws InvalidKeyException       If the key is invalid.
     * @throws InvalidResponseException   If the response is invalid.
     * @throws XmlParserException        If an XML parsing error occurs.
     * @throws InternalException         If an internal error occurs.
     */
    public InputStream downloadObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioUtil.getObject(bucketName, objectName);
    }
}

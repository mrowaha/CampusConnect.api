package com.campusconnect.image.util;

import com.campusconnect.image.config.MinioConfigProperties;
import io.minio.*;
import io.minio.errors.*;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Component
public class MinioUtil {

    private static final Logger logger = LoggerFactory.getLogger(MinioUtil.class);

    private final MinioClient minioClient;
    private final MinioConfigProperties properties;

    @Autowired
    public MinioUtil(MinioClient client, MinioConfigProperties properties) {
        this.minioClient = client;
        this.properties = properties;
    }

    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(
                        BucketExistsArgs.builder().
                                bucket(bucketName).
                                build());

    }

    @SneakyThrows
    public boolean makeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());
            return true;
        } else {
            return false;
        }
    }


    public void putObject(String bucket, byte[] filedata, String filename, String fileType)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (properties.isEnableLog()) {
            logger.info("MinioUtil | putObject is called");
            logger.info("MinioUtil | putObject | filename : " + filename);
            logger.info("MinioUtil | putObject | fileType : " + fileType);
        }

        InputStream is = new ByteArrayInputStream(filedata);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(filename)
                        .contentType(fileType)
                        .stream(is, -1, properties.getImageSize())
                        .build()
        );
    }

    public InputStream getObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (properties.isEnableLog()) {
            logger.info("MinioUtil | getObject is called");
        }
        StatObjectResponse statObject = statObject(bucketName, objectName);
        if (statObject != null && statObject.size() > 0) {
            InputStream stream =
                    minioClient.getObject(
                            GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(objectName)
                                    .build());

            if (properties.isEnableLog())
                logger.info("MinioUtil | getObject | stream : " + stream.toString());
            return stream;
        }
        return null;
    }

    public StatObjectResponse statObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (properties.isEnableLog()) {
            logger.info("MinioUtil | statObject is called");
        }

        StatObjectResponse stat =
                minioClient.statObject(
                        StatObjectArgs.builder().bucket(bucketName).object(objectName).build());

        if (properties.isEnableLog())
            logger.info("MinioUtil | statObject | stat : " + stat.toString());
        return stat;
    }

}

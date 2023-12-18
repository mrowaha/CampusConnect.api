package com.campusconnect.image;

import com.campusconnect.image.config.MinioConfigProperties;
import com.campusconnect.image.util.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BucketValidationRunner implements ApplicationRunner {


    private final MinioConfigProperties properties;
    private final MinioUtil minioUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("validating MinIO Buckets");
        if (!minioUtil.bucketExists(properties.getProfileBucketName())) {
            log.warn(properties.getProfileBucketName() + " does not exist");
            log.info("creating MinIO Buckets");
            minioUtil.makeBucket(properties.getProfileBucketName());
            log.info("successfully created MinIO Buckets");
        }
        if (!minioUtil.bucketExists(properties.getProductBucketName())) {
            log.warn(properties.getProductBucketName() + " does not exist");
            log.info("creating MinIO Buckets");
            minioUtil.makeBucket(properties.getProductBucketName());
            log.info("successfully created MinIO Buckets");
        }
        if (!minioUtil.bucketExists(properties.getForumsBucketName())) {
            log.warn(properties.getForumsBucketName() + " does not exist");
            log.info("creating MinIO Buckets");
            minioUtil.makeBucket(properties.getForumsBucketName());
            log.info("successfully created MinIO Buckets");
        }

        log.info("validated MinIO Buckets");

    }
}

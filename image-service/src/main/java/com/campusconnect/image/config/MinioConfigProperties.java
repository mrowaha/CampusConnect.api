package com.campusconnect.image.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "minio")
public class MinioConfigProperties {
    private String endpoint;

    private Integer port;

    private String accessKey;

    private String secretKey;

    private boolean secure;

    private String profileBucketName;

    private long partSize;

    private boolean enableLog;
}

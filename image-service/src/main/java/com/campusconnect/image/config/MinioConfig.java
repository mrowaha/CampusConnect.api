package com.campusconnect.image.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioConfigProperties.class)
public class MinioConfig {

    private final MinioConfigProperties properties;

    @Autowired
    public MinioConfig(MinioConfigProperties properties) {
        this.properties = properties;
    }
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(properties.getEndpoint(), properties.getPort(), properties.isSecure())
                .build();
    }


}

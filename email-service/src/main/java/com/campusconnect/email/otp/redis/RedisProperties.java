package com.campusconnect.email.otp.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    String host;

    Integer port;

    Long ttl;
}

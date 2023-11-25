package com.campusconnect.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("jwt.role")
@Data
public class RoledJwtProperties {

    private String bilkenteerSecret;

    private String moderatorSecret;

}

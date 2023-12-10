package com.campusconnect.ui.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("admin")
@Data
public class AdminProperties {

    private String apiKey;
}

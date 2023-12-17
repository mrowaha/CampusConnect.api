package com.campusconnect.ui.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth")
@Data
public class AuthProperties {

    Boolean requireOtp;
}

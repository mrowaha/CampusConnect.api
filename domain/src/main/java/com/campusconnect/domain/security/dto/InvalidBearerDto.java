package com.campusconnect.domain.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class InvalidBearerDto extends JwtError {

    String email;

    public InvalidBearerDto(String message, String email) {
        this.message = message;
        this.email = email;
    }
}

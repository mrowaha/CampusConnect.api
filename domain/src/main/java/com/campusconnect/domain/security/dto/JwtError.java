package com.campusconnect.domain.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtError {

    protected String message;

    public JwtError(String message) {
        this.message = message;
    }

}

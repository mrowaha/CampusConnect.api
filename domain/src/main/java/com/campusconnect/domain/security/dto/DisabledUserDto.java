package com.campusconnect.domain.security.dto;

public class DisabledUserDto extends JwtError {

    String email;

    Boolean enabled;

    public DisabledUserDto(String message, String email) {
        this.message = message;
        this.email = email;
        this.enabled = false;
    }


}

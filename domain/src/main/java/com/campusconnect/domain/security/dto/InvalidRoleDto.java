package com.campusconnect.domain.security.dto;

import com.campusconnect.domain.user.enums.Role;

public class InvalidRoleDto extends JwtError{

    Role[] roles;

    public InvalidRoleDto(String message, Role[] roles) {
        this.roles = roles;
        this.message = message;
    }
}

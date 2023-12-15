package com.campusconnect.domain.security.dto;

import com.campusconnect.domain.user.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutOfScopeDto extends JwtError {

    Role required;

    public OutOfScopeDto(String message, Role required) {
        this.message = message;
        this.required = required;
    }
}

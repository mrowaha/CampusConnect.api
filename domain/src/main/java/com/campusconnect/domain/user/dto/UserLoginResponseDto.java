package com.campusconnect.domain.user.dto;

import com.campusconnect.domain.user.enums.Role;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
public class UserLoginResponseDto {
    BearerToken token;

    UUID uuid;

    String firstName;

    String lastName;

    Role role;

    String email;

}

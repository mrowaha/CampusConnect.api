package com.campusconnect.domain.user.dto;

import com.campusconnect.domain.user.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserInfoDto {
    UUID uuid;

    String firstName;

    String lastName;

    Role role;

    String email;

    Boolean isActive;
}

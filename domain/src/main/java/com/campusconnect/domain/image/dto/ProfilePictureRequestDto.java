package com.campusconnect.domain.image.dto;

import com.campusconnect.domain.user.enums.Role;
import lombok.Data;

import java.util.UUID;
@Data
public class ProfilePictureRequestDto {

    UUID uuid;

    Role role;
}

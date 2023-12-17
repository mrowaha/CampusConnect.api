package com.campusconnect.domain.admin.dto;

import com.campusconnect.domain.user.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSuspendResponseDto {
    UUID uuid;
    Boolean successStatus;
    String message;
    UserInfoDto user;
}

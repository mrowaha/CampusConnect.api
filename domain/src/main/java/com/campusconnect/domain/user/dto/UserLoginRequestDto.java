package com.campusconnect.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginRequestDto {

    @NotBlank(message = "email field cannot be blank")
    @NotNull(message = "email field cannot be null")
    String email;

    @NotBlank(message = "password field cannot be blank")
    String password;
}

package com.campusconnect.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilkenteerLoginDto {

    @NotBlank(message = "email field cannot be blank")
    @NotNull(message = "email field cannot be null")
    String email;

    @NotBlank(message = "password field cannot be blank")
    String password;
}

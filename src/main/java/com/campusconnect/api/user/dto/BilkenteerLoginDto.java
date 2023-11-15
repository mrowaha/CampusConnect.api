package com.campusconnect.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilkenteerLoginDto {

    @NotBlank(message = "email field cannot be blank")
    String email;

    @NotBlank(message = "password field cannot be blank")
    String password;
}

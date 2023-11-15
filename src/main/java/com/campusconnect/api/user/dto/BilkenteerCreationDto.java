package com.campusconnect.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilkenteerCreationDto {

    @NotBlank(message = "first name field cannot be blank")
    private String firstName;

    @NotBlank(message = "last name field cannot be blank")
    private String lastName;

    @NotBlank(message = "email field cannot be blank")
    private String email;

    @NotBlank(message = "password field cannot be blank")
    private String password;
}
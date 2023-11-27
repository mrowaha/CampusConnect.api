package com.campusconnect.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationDto {

    @NotBlank(message = "first name field cannot be blank")
    @NotNull(message = "first name cannot be null")
    @Pattern(regexp = "[A-Za-z]+", message = "first name can only contain letters from english alphabet")
    private String firstName;

    @NotBlank(message = "last name field cannot be blank")
    @NotNull(message = "last name cannot be null")
    @Pattern(regexp = "[A-Za-z]+", message = "last name can only contain letters from english alphabet")
    private String lastName;

    @NotBlank(message = "email field cannot be blank")
    @NotNull(message = "email cannot be null")
    @Pattern(regexp = "[A-Za-z0-9._]+@([a-z]+.)*bilkent.edu.tr", message = "email must be under the domain .bilkent.edu.tr and local part can only contain english alphabets, digits, dot or underscore")
    private String email;

    @NotBlank(message = "password field cannot be blank")
    @NotNull(message = "password cannot be null")
    private String password;
}
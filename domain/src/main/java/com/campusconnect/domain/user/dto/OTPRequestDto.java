package com.campusconnect.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTPRequestDto {
    @NotBlank(message = "email field cannot be blank")
    @NotNull(message = "email cannot be null")
    @Pattern(regexp = "[A-Za-z0-9._]+@([a-z]+.)*bilkent.edu.tr", message = "email must be under the domain .bilkent.edu.tr and local part can only contain english alphabets, digits, dot or underscore")

    String email;
}

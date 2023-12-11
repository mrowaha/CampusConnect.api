package com.campusconnect.domain.user.dto;

import com.campusconnect.domain.security.dto.BearerToken;
import com.campusconnect.domain.user.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


import java.util.UUID;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BilkenteerLoginResponse {
    BearerToken token;

    UUID uuid;

    String firstName;

    String lastName;

    Role role;

    String email;

    Integer trustScore;
}

package com.campusconnect.domain.user.dto;

import lombok.Builder;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class BilkenteerLoginResponse extends UserLoginResponseDto {

    Integer trustScore;
}

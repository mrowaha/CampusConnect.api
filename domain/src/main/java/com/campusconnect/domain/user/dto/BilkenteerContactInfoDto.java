package com.campusconnect.domain.user.dto;

import com.campusconnect.domain.user.pojo.BilkenteerAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BilkenteerContactInfoDto {

    BilkenteerAddress address;
}

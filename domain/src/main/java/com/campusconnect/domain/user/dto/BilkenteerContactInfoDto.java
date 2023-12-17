package com.campusconnect.domain.user.dto;

import com.campusconnect.domain.user.pojo.BilkenteerAddress;
import com.campusconnect.domain.user.pojo.BilkenteerPhoneNumbers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BilkenteerContactInfoDto {
    List<String> phoneNumbers;
    BilkenteerAddress address;
}

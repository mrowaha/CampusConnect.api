package com.campusconnect.domain.user.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BilkenteerAddress
{
    private String postalCode;

    private String district;

    private String city;

}

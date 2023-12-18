package com.campusconnect.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageCountDto {
    ArrayList<String> fileNamePrefixes;
    Integer totalCount;
}

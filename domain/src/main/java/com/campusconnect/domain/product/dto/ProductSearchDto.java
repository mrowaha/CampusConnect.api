package com.campusconnect.domain.product.dto;

import com.campusconnect.domain.product.enums.SortBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchDto {

    private String keywords;

    private List<String> tags;

    private Double minPrice;

    private Double maxPrice;

    private Integer sellerRating;

    private SortBy sortBy;
}

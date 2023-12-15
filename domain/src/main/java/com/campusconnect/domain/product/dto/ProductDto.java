package com.campusconnect.domain.product.dto;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.product.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String sellerId;

    @NotBlank(message = "Product name cannot be blank")
    @NotNull(message = "Product name cannot be null")
    private String name;

    @NotBlank(message = "Product description cannot be blank")
    @NotNull(message = "Product description cannot be null")
    private String description;

    @NotNull(message = "Product price cannot be null")
    private Double price;

    @NotNull(message = "Product type must be chosen")
    private ProductType type;

    private ArrayList<String> tagNames;
}

package com.campusconnect.domain.ProductTag.dto;

import java.util.UUID;

import com.campusconnect.domain.ProductTag.entity.ProductTagStatus;
import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagDto {

    private UUID id;
    private String name;
    private ProductTagStatus tagStatus;
    private UUID requestedByID;
    private UUID acceptedByID;
    private UUID categoriesID;
}

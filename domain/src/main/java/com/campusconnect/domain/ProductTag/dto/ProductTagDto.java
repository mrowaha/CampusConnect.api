package com.campusconnect.domain.ProductTag.dto;

import java.util.UUID;

import com.campusconnect.domain.ProductTag.enums.ProductTagStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagDto {
    @NotBlank(message = "Tag name cannot be blank")
    @NotNull(message = "Tag name cannot be null")
    private String name;

    private ProductTagStatus tagStatus;
    private UUID requestedByID;
    private UUID acceptedByID;
}

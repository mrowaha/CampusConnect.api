package com.campusconnect.domain.report.dto;

import com.campusconnect.domain.report.enums.ReportableEntities;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {
    private UUID id;

    @NotBlank(message = "title field cannot be blank")
    private String title;

    @NotBlank(message = "description field cannot be blank")
    private String description;

//    private String image;

    @NotBlank(message = "reportedEntity field cannot be blank")
    @NotNull(message = "reportedEntity cannot be null")
    private ReportableEntities reportedEntity;

    @NotBlank(message = "reportedEntityID field cannot be blank")
    @NotNull(message = "reportedEntityID cannot be null")
    private UUID reportedEntityID;

    @NotBlank(message = "reportedAt field cannot be blank")
    @NotNull(message = "reportedAt cannot be null")
    private LocalDateTime reportedAt;

    @NotBlank(message = "reporterID field cannot be blank")
    @NotNull(message = "reporterID cannot be null")
    private UUID reporterID;
}

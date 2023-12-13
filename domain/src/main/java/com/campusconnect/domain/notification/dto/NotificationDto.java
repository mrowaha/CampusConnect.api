package com.campusconnect.domain.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    @NotBlank(message = "subject field cannot be blank")
    @NotNull(message = "subject cannot be null")
    private String subject;

    @NotBlank(message = "description field cannot be blank")
    @NotNull(message = "description cannot be null")
    private String description;

}

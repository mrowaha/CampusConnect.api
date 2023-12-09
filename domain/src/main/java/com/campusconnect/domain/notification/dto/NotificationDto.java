package com.campusconnect.domain.notification.dto;

import java.time.LocalDateTime;

import com.campusconnect.domain.notification.enums.NotificationType;
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
public class NotificationDto {

    @NotBlank(message = "subject field cannot be blank")
    @NotNull(message = "subject cannot be null")
    private Long id;

    @NotBlank(message = "subject field cannot be blank")
    @NotNull(message = "subject cannot be null")
    private NotificationType type;

    @NotBlank(message = "subject field cannot be blank")
    @NotNull(message = "subject cannot be null")
    private LocalDateTime createdAt;

    @NotBlank(message = "subject field cannot be blank")
    @NotNull(message = "subject cannot be null")
    private String content;

    @NotBlank(message = "subject field cannot be blank")
    @NotNull(message = "subject cannot be null")
    private Boolean seen;

    @NotBlank(message = "subject field cannot be blank")
    @NotNull(message = "subject cannot be null")
    private Long receiverId;
}
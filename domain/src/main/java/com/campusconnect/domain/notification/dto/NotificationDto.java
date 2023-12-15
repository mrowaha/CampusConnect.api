package com.campusconnect.domain.notification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

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


    private UUID id;

    @NotBlank(message = "type field cannot be blank")
    @NotNull(message = "type cannot be null")
    private NotificationType type;

    private LocalDateTime createdAt;

    @NotBlank(message = "content field cannot be blank")
    @NotNull(message = "content cannot be null")
    private String content;

    private boolean seen = false;

    private Long receiverId;
}
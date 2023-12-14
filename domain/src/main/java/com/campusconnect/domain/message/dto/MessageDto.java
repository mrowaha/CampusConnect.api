package com.campusconnect.domain.message.dto;

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
public class MessageDto {

    private UUID id;

    private UUID threadID;

    private boolean seen = false;

    private LocalDateTime timestamp;

    private String content;

    private UUID senderId;

    private UUID receiverId;

    //TODO Add Image
}
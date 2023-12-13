package com.campusconnect.domain.messageThread.dto;

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
public class MessageThreadDto {

    private UUID id;

    private UUID initiatingUserID;

    private UUID receivingUserID;

//    private String initiatingUserName;
//
//    private String receivingUserName;
}
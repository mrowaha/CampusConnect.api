package com.campusconnect.domain.notification.entity;

import com.campusconnect.domain.notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "notifications")
public class Notification{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private NotificationType type;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    @Column(name = "content", nullable = false)
    @NonNull
    private String content;

    @Column(name = "seen", nullable = false)
    private boolean seen = false;

    @Column(name = "receiver_id", nullable = false)
    @NonNull
    private UUID receiverId;
}

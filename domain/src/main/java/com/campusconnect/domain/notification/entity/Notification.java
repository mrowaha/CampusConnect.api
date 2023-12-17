package com.campusconnect.domain.notification.entity;

import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.notification.enums.NotificationType;
import com.campusconnect.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "cc_notifications")
public class Notification{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    // The type of the notification (e.g., PRODUCT, TAG)
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private NotificationType type;

    // The timestamp when the notification was created
    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    // The content or message of the notification
    @Column(name = "content", nullable = false)
    @NonNull
    private String content;

    // Flag indicating whether the notification has been seen by the user
    @Column(name = "seen", nullable = false)
    private boolean seen = false;

    // The user to whom the notification is associated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "notifications")
    @JsonIgnore
    private User user;

}

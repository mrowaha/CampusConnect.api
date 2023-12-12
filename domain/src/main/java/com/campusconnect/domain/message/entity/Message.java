package com.campusconnect.domain.message.entity;

import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.notification.enums.NotificationType;
import com.campusconnect.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@RequiredArgsConstructor
@Table(name = "cc_message")
public class Message{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_thread_id")
    @JsonBackReference(value = "messages")
    @NonNull
    private MessageThread messageThread;

    @Column(name = "seen")
    private boolean seen = false;

    @Column(name = "timeStamp", nullable = false)
    @NonNull
    private LocalDateTime timeStamp;

    @Column(name = "content", nullable = false)
    @NonNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_Id")
    @JsonBackReference(value = "sender")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_Id")
    @JsonBackReference(value = "receiver")
    private User receiver;

    @Transient
    private UUID senderId;

    @Transient
    private UUID receiverId;

    @PostLoad
    private void fillTransientFields() {
        if (sender != null) {
            this.senderId = sender.getUserId();
        }
        if (receiver != null) {
            this.receiverId = receiver.getUserId();
        }
    }
    //TODO Image URL
}

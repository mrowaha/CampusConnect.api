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

    // Unique identifier for the message
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    // The message thread to which this message belongs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_thread_id")
    @JsonBackReference(value = "messages")
    @NonNull
    private MessageThread messageThread;

    // Flag indicating whether the message has been seen by the recipient
    @Column(name = "seen")
    private boolean seen = false;

    // Timestamp when the message was sent
    @Column(name = "timeStamp", nullable = false)
    @NonNull
    private LocalDateTime timeStamp;

    // Content of the message
    @Column(name = "content", nullable = false)
    @NonNull
    private String content;

    // Sender of the message
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_Id")
    @JsonBackReference(value = "sender")
    private User sender;

    // Receiver of the message
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_Id")
    @JsonBackReference(value = "receiver")
    private User receiver;

    // Transient field to store the ID of the sender (not persisted in the database)
    @Transient
    private UUID senderId;

    // Transient field to store the ID of the receiver (not persisted in the database)
    @Transient
    private UUID receiverId;

    // Method to populate transient fields after the entity is loaded
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

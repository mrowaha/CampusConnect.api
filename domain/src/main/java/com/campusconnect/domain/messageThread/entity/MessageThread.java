package com.campusconnect.domain.messageThread.entity;

import com.campusconnect.domain.message.entity.Message;
import com.campusconnect.domain.notification.enums.NotificationType;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.Moderator;
import com.campusconnect.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cc_message_thread")
public class MessageThread{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiating_user_id", nullable = false)
    @JsonBackReference(value = "initiating_user_id")
    private User initiatingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiving_user_id", nullable = false)
    @JsonBackReference(value = "receiving_user_id")
    private User receivingUser;

    @OneToMany(mappedBy = "messageThread", cascade = CascadeType.ALL)
    private Set<Message> messages = new HashSet();
}

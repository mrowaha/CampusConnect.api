package com.campusconnect.domain.messageThread.entity;

import com.campusconnect.domain.message.entity.Message;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
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

    // Unique identifier for the message thread
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    // The user who initiated the message thread
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiating_user_id", nullable = false)
    private User initiatingUser;

    // The user who is the recipient in the message thread
    @ManyToOne()
    @JoinColumn(name = "receiving_user_id", nullable = false)
    private User receivingUser;

    // Set of messages associated with this message thread, ordered by timestamp
    @OneToMany(mappedBy = "messageThread", cascade = CascadeType.ALL)
    @OrderBy("timeStamp")
    private Set<Message> messages = new HashSet();

}

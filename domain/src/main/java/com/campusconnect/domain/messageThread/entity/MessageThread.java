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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiating_user_id", nullable = false)
//    @JsonBackReference(value = "initiating_user_id")
    private User initiatingUser;

    @ManyToOne()
    @JoinColumn(name = "receiving_user_id", nullable = false)
//    @JsonBackReference(value = "receiving_user_id")
    private User receivingUser;

    @OneToMany(mappedBy = "messageThread", cascade = CascadeType.ALL)
    @OrderBy("timeStamp")
    private Set<Message> messages = new HashSet();

}

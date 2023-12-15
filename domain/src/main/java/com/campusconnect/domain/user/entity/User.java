package com.campusconnect.domain.user.entity;

import com.campusconnect.domain.message.entity.Message;
import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.notification.entity.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.campusconnect.domain.user.enums.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.util.UUID;

@NoArgsConstructor
@Data
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@Entity(name = "cc_user") //Need to add this to create relations with User
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //User itself won't have a table
public abstract class User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID userId;

    @Column(name = "first_name", nullable = false)
    @NonNull
    protected String firstName;

    @Column(name = "last_name", nullable = false)
    @NonNull
    protected String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    protected String email;

    @Column(name = "password", nullable = false)
    @NonNull
    protected String password;

    @Column(name = "email_notification_enable")
    private Boolean enableEmailNotification = true;

    @Column(name = "app_notification_enable")
    private Boolean enableAppNotification = true;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

    public String getUsername() {
        return this.email;
    }
    public abstract Collection<? extends GrantedAuthority> getAuthorities();
    public abstract boolean isEnabled();

    @OneToMany(mappedBy = "initiatingUser")
    @JsonManagedReference(value = "initiating_user_id")
    @JsonIgnore
    private Set<MessageThread> initiatedThreads = new HashSet<>();

    @OneToMany(mappedBy = "receivingUser")
    @JsonManagedReference(value = "receiving_user_id")
    @JsonIgnore
    private Set<MessageThread> receivedThreads = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Notification> notifications = new HashSet();

//    @Transient
//    public Set<MessageThread> getAllMessageThreads() {
//        Set<MessageThread> allThreads = new HashSet<>();
//        allThreads.addAll(initiatedThreads);
//        allThreads.addAll(receivedThreads);
//        return allThreads;
//    }
}

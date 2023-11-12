package com.campusconnect.api.user.entity;

import jakarta.persistence.*;
import com.campusconnect.api.user.enums.Role;
import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @Column(name = "first_name", nullable = false)
    protected String firstName;

    @Column(name = "last_name", nullable = false)
    @NonNull
    protected String lastName;

    @Column(name = "email", nullable = false)
    @NonNull
    protected String email;

    @Column(name = "email_notification_enable")
    private Boolean enableEmailNotification = true;

    @Column(name = "app_notification_enable")
    private Boolean enableAppNotification = true;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

}

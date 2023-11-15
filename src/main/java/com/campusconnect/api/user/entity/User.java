package com.campusconnect.api.user.entity;

import jakarta.persistence.*;
import com.campusconnect.api.user.enums.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Data
@EqualsAndHashCode
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User{

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

}

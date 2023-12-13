package com.campusconnect.domain.user.entity;

import jakarta.persistence.*;
import com.campusconnect.domain.user.enums.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Data
//@MappedSuperclass
@NoArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
@Entity(name = "cc_user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
}

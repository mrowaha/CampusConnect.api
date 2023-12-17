package com.campusconnect.domain.user.entity;

import com.campusconnect.domain.user.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="cc_moderator")
@RequiredArgsConstructor
@SuperBuilder
public class Moderator extends User {

    // Flag indicating whether the Moderator is active or not
    @Column(name = "is_active")
    private Boolean isActive = true;

    // Overrides the method in User class to provide Moderator-specific authorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = Role.MODERATOR::name;
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    // Overrides the method in User class to indicate whether the Moderator is enabled or not
    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    // Overrides the default toString method to provide a formatted string representation
    @Override
    public String toString() {
        return String.format("UUID: %s, ROLE: %s", this.userId.toString(), this.getRole().toString());
    }
}

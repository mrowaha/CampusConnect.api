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

    @Column(name = "is_active")
    private Boolean isActive = true;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = Role.MODERATOR::name;
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }
    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    @Override
    public String toString() {
        return String.format("UUID: %s, ROLE: %s", this.userId.toString(), this.getRole().toString());
    }
}

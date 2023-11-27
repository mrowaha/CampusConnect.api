package com.campusconnect.domain.user.repository;

import com.campusconnect.domain.user.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModeratorRepository extends JpaRepository<Moderator, UUID> {

    Boolean existsByEmail(String email);
    Moderator findByEmail(String email);
}

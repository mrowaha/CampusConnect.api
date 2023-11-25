package com.campusconnect.api.user.repository;

import com.campusconnect.api.user.entity.Bilkenteer;
import com.campusconnect.api.user.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModeratorRepository extends JpaRepository<Moderator, UUID> {

    Boolean existsByEmail(String email);
    Moderator findByEmail(String email);
}

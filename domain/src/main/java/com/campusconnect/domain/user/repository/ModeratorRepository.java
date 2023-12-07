package com.campusconnect.domain.user.repository;

import com.campusconnect.domain.user.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, UUID> {

    Boolean existsByEmail(String email);
    Moderator findByEmail(String email);
}

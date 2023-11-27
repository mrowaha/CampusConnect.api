package com.campusconnect.api.user.repository;

import com.campusconnect.api.user.entity.Bilkenteer;
import com.campusconnect.api.user.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, UUID> {

    Boolean existsByEmail(String email);
    Moderator findByEmail(String email);
}

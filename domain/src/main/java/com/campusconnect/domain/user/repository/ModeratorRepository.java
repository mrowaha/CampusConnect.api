package com.campusconnect.domain.user.repository;

import com.campusconnect.domain.user.entity.Moderator;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModeratorRepository extends ListCrudRepository<Moderator, UUID> {

    Boolean existsByEmail(String email);
    Optional<Moderator> findByEmail(String email);
}

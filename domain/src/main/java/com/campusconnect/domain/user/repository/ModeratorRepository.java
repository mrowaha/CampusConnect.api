package com.campusconnect.domain.user.repository;

import com.campusconnect.domain.user.entity.Moderator;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModeratorRepository extends ListCrudRepository<Moderator, UUID> {

    Boolean existsByEmail(String email);
    Optional<Moderator> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Moderator SET profilePicture = :profilePicture WHERE email = :email")
    int updateProfilePictureByEmail(@Param("email") String email, @Param("profilePicture") String profilePicture);
}

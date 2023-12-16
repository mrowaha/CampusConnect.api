package com.campusconnect.domain.user.repository;

import com.campusconnect.domain.user.entity.Bilkenteer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Repository
public interface BilkenteerRepository extends ListCrudRepository<Bilkenteer, UUID> {

    Boolean existsByEmail(String email);
    Optional<Bilkenteer> findByEmail(String email);
    Optional<Bilkenteer> findByEmailAndPassword(String email, String password);

    @Modifying
    @Transactional
    @Query("UPDATE Bilkenteer SET profilePicture = :profilePicture WHERE email = :email")
    int updateProfilePictureByEmail(@Param("email") String email, @Param("profilePicture") String profilePicture);

    List<Bilkenteer> findBySubscribedTags_NameIn(Set<String> tagNames);

}

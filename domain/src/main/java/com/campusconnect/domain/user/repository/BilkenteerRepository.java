package com.campusconnect.domain.user.repository;

import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.pojo.BilkenteerAddress;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface BilkenteerRepository extends ListCrudRepository<Bilkenteer, UUID> {

    Boolean existsByEmail(String email);
    Optional<Bilkenteer> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Bilkenteer SET address = :address WHERE userId = :id")
    void updateAddressBy(UUID id, BilkenteerAddress address);

    @Modifying
    @Transactional
    @Query("UPDATE Bilkenteer SET isSuspended = true WHERE userId = :uuid")
    void disable(UUID uuid);

    @Modifying
    @Transactional
    @Query("UPDATE Bilkenteer SET isSuspended = false WHERE userId = :uuid")
    void enable(UUID uuid);

    @Modifying
    @Transactional
    @Query("UPDATE Bilkenteer SET profilePicture = :profilePicture WHERE email = :email")
    int updateProfilePictureByEmail(@Param("email") String email, @Param("profilePicture") String profilePicture);
}

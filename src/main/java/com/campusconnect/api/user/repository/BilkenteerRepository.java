package com.campusconnect.api.user.repository;

import com.campusconnect.api.user.entity.Bilkenteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.Optional;


public interface BilkenteerRepository extends JpaRepository<Bilkenteer, UUID> {

    Boolean existsByEmail(String email);
    Bilkenteer findByEmail(String email);
}

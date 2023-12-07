package com.campusconnect.domain.user.repository;

import com.campusconnect.domain.user.entity.Bilkenteer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface BilkenteerRepository extends ListCrudRepository<Bilkenteer, UUID> {

    Boolean existsByEmail(String email);

    Optional<Bilkenteer> findByEmail(String email);
    Optional<Bilkenteer> findByEmailAndPassword(String email, String password);
}

package com.campusconnect.domain.messageThread.repository;

import com.campusconnect.domain.messageThread.entity.MessageThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageThreadRepository extends JpaRepository<MessageThread, UUID> {


    @Query("SELECT mt FROM MessageThread mt " +
            "WHERE ((mt.initiatingUser.userId = :receivingUserId AND mt.receivingUser.userId = :initiatingUserId)" +
            "OR (mt.initiatingUser.userId = :initiatingUserId AND mt.receivingUser.userId = :receivingUserId))")
    Optional<MessageThread> findByInitiatingUserIdAndReceivingUserId(UUID initiatingUserId, UUID receivingUserId);

    @Query("SELECT mt FROM MessageThread mt WHERE (mt.initiatingUser.userId = :userId OR mt.receivingUser.userId = :userId)")
    Optional<List<MessageThread>> findAllByUserId(UUID userId);

}

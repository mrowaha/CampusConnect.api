package com.campusconnect.domain.notification.repository;

import com.campusconnect.domain.user.entity.Bilkenteer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface NotificationRepository extends ListCrudRepository<com.campusconnect.domain.notification.entity.Notification, UUID> {

//    Boolean sortBySeen(boolean seen);
}

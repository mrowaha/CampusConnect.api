package com.campusconnect.domain.notification.repository;

import com.campusconnect.domain.notification.entity.Notification;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends ListCrudRepository<Notification, UUID> {

    Optional<List<Notification>> findAllByUserUserId(UUID id);
}

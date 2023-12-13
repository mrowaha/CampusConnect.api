package com.campusconnect.ui.notification.service;

import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.notification.dto.NotificationDto;
import com.campusconnect.domain.notification.entity.Notification;
import com.campusconnect.domain.notification.repository.NotificationRepository;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.BilkenteerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BilkenteerService bilkenteerService;

    private Notification saveNotification(UUID userId, NotificationDto notificationDto){

        Notification notification = new Notification();

        User user = bilkenteerService.findUser(userId);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException();
        }

        notification.setUser(user);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSeen(false);
        notification.setType(notificationDto.getType());
        notification.setContent(notificationDto.getContent());

        notificationRepository.save(notification);

        return notification;
    }

    public List<Notification> getNotificationList(){
        return (List<Notification>)notificationRepository.findAll();
    }



    public void deleteNotification(UUID notificationId){
        notificationRepository.deleteById(notificationId);
    }
}

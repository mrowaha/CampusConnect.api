package com.campusconnect.ui.notification.service;

import com.campusconnect.domain.notification.dto.NotificationDto;
import com.campusconnect.domain.notification.entity.Notification;
import com.campusconnect.domain.notification.repository.NotificationRepository;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.notification.exceptions.NotificationNotFoundException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.BilkenteerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BilkenteerService bilkenteerService;

    public Notification saveNotification(UUID userId, NotificationDto notificationDto) throws UserNotFoundException{

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

    public List<Notification> getUserNotificationList(UUID userId){
        return notificationRepository.findAllByUserUserId(userId).orElse(null);
    }



    public void deleteNotification(UUID notificationId){
        notificationRepository.deleteById(notificationId);
    }


    @Transactional
    public void markNotificationsAsSeen(List<UUID> notificationIds) throws NotificationNotFoundException{
        for (UUID notificationId : notificationIds) {

            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(NotificationNotFoundException::new);


            notification.setSeen(true);
            notificationRepository.save(notification);

        }
    }

}

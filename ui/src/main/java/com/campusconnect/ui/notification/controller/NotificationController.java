package com.campusconnect.ui.notification.controller;

import com.campusconnect.domain.notification.entity.Notification;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.notification.exceptions.NotificationNotFoundException;
import com.campusconnect.ui.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController extends SecureController {

    private final NotificationService notificationService;

    @GetMapping("/user/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<Notification>> getUserNotificationList(@RequestParam("userId") UUID userId){
        return ResponseEntity.ok(notificationService.getUserNotificationList(userId));
    }

    @DeleteMapping("/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Notification> deleteNotification(@RequestParam("notificationId") UUID notificationId){
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(null);
    }

    @PutMapping(value = "/markNotificationsSeen")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Void> markNotificationsAsSeen(@RequestBody List<UUID> notificationIds) throws NotificationNotFoundException {
        notificationService.markNotificationsAsSeen(notificationIds);
        return ResponseEntity.ok(null);
    }



}





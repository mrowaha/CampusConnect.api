package com.campusconnect.ui.notification.controller;

import com.campusconnect.domain.notification.entity.Notification;
import com.campusconnect.domain.product.dto.ProductDto;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.market.service.ProductService;
import com.campusconnect.ui.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping()
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<Notification>> getNotificationList(){
        return ResponseEntity.ok(notificationService.getNotificationList());
    }

    @DeleteMapping("/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Notification> deleteNotification(@RequestParam("notificationId") UUID notificationId){
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(null);
    }



}





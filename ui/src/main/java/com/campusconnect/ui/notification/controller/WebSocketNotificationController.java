package com.campusconnect.ui.notification.controller;

import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketNotificationController {

    @Autowired
    private SimpMessagingTemplate template;

    // Method to send notifications
    public void notifyUser(String userId, String message) {
        template.convertAndSend("/topic/messages/" + userId, message); //TODO Add userId
    }
}

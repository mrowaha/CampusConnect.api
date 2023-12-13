package com.campusconnect.ui.messageThread.controller;

import com.campusconnect.domain.message.dto.MessageDto;
import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.messageThread.service.MessageThreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/messageThread")
public class MessageThreadController extends SecureController {

    private final MessageThreadService messageThreadService;


    @GetMapping(value = "/user/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Optional<List<MessageThread>>> getAllMessageThreads(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(messageThreadService.getAllMessageThreads(userId));
    }

    @PutMapping(value = "/markMessagesSeen/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Void> markMessagesAsSeen(@RequestParam("userId") UUID userId, @Valid @RequestBody List<MessageDto> messageDtos) {
        messageThreadService.markMessagesAsSeen(userId, messageDtos);
        return ResponseEntity.ok(null);
    }

    @PostMapping(value = "/sendMessage")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody MessageDto messageDto) {
        messageThreadService.sendMessage(messageDto);
        return ResponseEntity.ok(null);
    }

}

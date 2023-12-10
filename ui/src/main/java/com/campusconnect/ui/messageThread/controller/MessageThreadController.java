package com.campusconnect.ui.messageThread.controller;

import com.campusconnect.domain.message.dto.MessageDto;
import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.ui.messageThread.service.MessageThreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/messageThread")
public class MessageThreadController {

    private final MessageThreadService messageThreadService;

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<Optional<List<MessageThread>>> getAllMessageThreads(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(messageThreadService.getAllMessageThreads(userId));
    }

    @GetMapping(value = "/user")
    public ResponseEntity<String> smth() {//@PathVariable("userId") UUID userId
        return ResponseEntity.ok("GotSmth");
    }

    @PostMapping(value = "/sendMessage")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody MessageDto messageDto) {
        messageThreadService.sendMessage(messageDto);
        return ResponseEntity.ok(null);
    }

}

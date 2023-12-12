package com.campusconnect.ui.messageThread.controller;

import com.campusconnect.domain.message.dto.MessageDto;
import com.campusconnect.domain.messageThread.entity.MessageThread;
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

    private static final String BASE_URL = "/messageThread";
    private static final String GET_MESSAGE_THREAD = "/user/{userId}";
    private static final String SEND_MESSAGE = "/sendMessage";
    private static final String MARK_SEEN = "/markMessagesSeen/{userId}";

    @GetMapping(value = MessageThreadController.GET_MESSAGE_THREAD)
    public ResponseEntity<Optional<List<MessageThread>>> getAllMessageThreads(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(messageThreadService.getAllMessageThreads(userId));
    }

    @PutMapping(value = MessageThreadController.MARK_SEEN)
    public ResponseEntity<Void> markMessagesAsSeen(@PathVariable("userId") UUID userId, @Valid @RequestBody List<MessageDto> messageDtos) {
        messageThreadService.markMessagesAsSeen(userId, messageDtos);
        return ResponseEntity.ok(null);
    }

    @PostMapping(value = MessageThreadController.SEND_MESSAGE)
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody MessageDto messageDto) {
        messageThreadService.sendMessage(messageDto);
        return ResponseEntity.ok(null);
    }

    @Override
    public void postConstruct() {
        this.addEndpoint(HttpMethod.GET, BASE_URL, GET_MESSAGE_THREAD, SecurityScope.NONE);
        this.addEndpoint(HttpMethod.POST, BASE_URL, SEND_MESSAGE, SecurityScope.NONE);
        this.addEndpoint(HttpMethod.PUT, BASE_URL, MARK_SEEN, SecurityScope.NONE);
        this.addEndpoint(HttpMethod.GET, "/ws", "/topic/messages/{userId}", SecurityScope.NONE);
    }
}

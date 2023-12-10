package com.campusconnect.ui.messageThread.service;

import com.campusconnect.domain.message.dto.MessageDto;
import com.campusconnect.domain.message.entity.Message;
import com.campusconnect.domain.message.repository.MessageRepository;
import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.messageThread.repository.MessageThreadRepository;
import com.campusconnect.domain.user.dto.BearerToken;
import com.campusconnect.domain.user.dto.UserCreationDto;
import com.campusconnect.domain.user.dto.UserLoginDto;
import com.campusconnect.domain.user.entity.Moderator;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.domain.user.repository.ModeratorRepository;
import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MessageThreadService {

    private final MessageRepository messageRepository;
    private final MessageThreadRepository messageThreadRepository;
    private final BilkenteerRepository bilkenteerRepository;
    private final ModeratorRepository moderatorRepository;

    public Optional<List<MessageThread>> getAllMessageThreads(UUID userId) {
        return messageThreadRepository.findAllByUserId(userId);
    }


    @Transactional
    public void sendMessage(MessageDto messageDto) {

        MessageThread messageThread = messageThreadRepository.findByInitiatingUserIdAndReceivingUserId(messageDto.getSenderId(), messageDto.getReceiverId()).orElse(null);

        //Create New Message Thread if first message between the sender and receiver
        if (Objects.isNull(messageThread)) {
            messageThread = addMessageThread(messageDto.getSenderId(), messageDto.getReceiverId());
        }

        Message message = new Message();

        message.setMessageThread(messageThread);
        message.setSender(findUser(messageDto.getSenderId()));
        message.setReceiver(findUser(messageDto.getReceiverId()));
        message.setContent(messageDto.getContent());
        message.setTimeStamp(LocalDateTime.now());

        messageRepository.save(message);
    }


    private MessageThread addMessageThread(UUID senderId, UUID receiverId) throws UserNotFoundException {

        MessageThread messageThread = new MessageThread();

        User initiatingUser = findUser(senderId);
        if (Objects.isNull(initiatingUser)) {
            throw new UserNotFoundException();
        }

        User receivingUser = findUser(receiverId);
        if (Objects.isNull(receivingUser)) {
            throw new UserNotFoundException();
        }

        messageThread.setInitiatingUser(initiatingUser);
        messageThread.setReceivingUser(receivingUser);
        messageThreadRepository.save(messageThread);

        return messageThread;
    }

    public User findUser(UUID userId) {

        User user = moderatorRepository.findById(userId).orElse(null);

        if (Objects.isNull(user)) {
            user = bilkenteerRepository.findById(userId).orElse(null);
        }

        return user;
    }


}
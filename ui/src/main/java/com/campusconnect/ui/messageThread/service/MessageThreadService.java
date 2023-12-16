package com.campusconnect.ui.messageThread.service;

import com.campusconnect.domain.message.dto.MessageDto;
import com.campusconnect.domain.message.entity.Message;
import com.campusconnect.domain.message.repository.MessageRepository;
import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.messageThread.repository.MessageThreadRepository;
import com.campusconnect.domain.notification.dto.NotificationDto;
import com.campusconnect.domain.notification.enums.NotificationType;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.messageThread.exceptions.MessageNotFoundException;
import com.campusconnect.ui.notification.service.NotificationService;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.BilkenteerService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class MessageThreadService {

    private final MessageRepository messageRepository;
    private final MessageThreadRepository messageThreadRepository;
    private final BilkenteerService bilkenteerService;
    private final NotificationService notificationService;

    private static final Logger log = Logger.getLogger(MessageThreadService.class.getName());

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
        message.setSender(bilkenteerService.findUser(messageDto.getSenderId()));
        message.setReceiver(bilkenteerService.findUser(messageDto.getReceiverId()));
        message.setContent(messageDto.getContent());
        message.setTimeStamp(LocalDateTime.now());

        messageRepository.save(message);

        //Creating Notification for User
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setType(NotificationType.INBOX);
        notificationDto.setContent("New message from " + message.getSender().getFirstName() + " : " + messageDto.getContent());

        notificationService.saveNotification(messageDto.getReceiverId(), notificationDto);

        log.info("User Notified about Message Successfully");
    }

    @Transactional
    public void markMessagesAsSeen(UUID userId, List<MessageDto> messageDtos) {
        for (MessageDto messageDto : messageDtos) {

            Message message = messageRepository.findById(messageDto.getId())
                    .orElseThrow(MessageNotFoundException::new);

            //Only Mark as seen if user is the one who received message
            if (userId.equals(message.getReceiverId())){
                message.setSeen(true);
                messageRepository.save(message);
            }
        }
    }

    private MessageThread addMessageThread(UUID senderId, UUID receiverId) throws UserNotFoundException {

        MessageThread messageThread = new MessageThread();

        User initiatingUser = bilkenteerService.findUser(senderId);
        if (Objects.isNull(initiatingUser)) {
            throw new UserNotFoundException();
        }

        User receivingUser = bilkenteerService.findUser(receiverId);
        if (Objects.isNull(receivingUser)) {
            throw new UserNotFoundException();
        }

        messageThread.setInitiatingUser(initiatingUser);
        messageThread.setReceivingUser(receivingUser);
        messageThreadRepository.save(messageThread);

        return messageThread;
    }

}
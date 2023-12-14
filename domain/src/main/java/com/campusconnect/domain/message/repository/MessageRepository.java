package com.campusconnect.domain.message.repository;

import com.campusconnect.domain.message.entity.Message;
import com.campusconnect.domain.messageThread.entity.MessageThread;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends ListCrudRepository<Message, UUID> {

    Optional<List<Message>> findAllByMessageThreadId(UUID messageThreadId);

}

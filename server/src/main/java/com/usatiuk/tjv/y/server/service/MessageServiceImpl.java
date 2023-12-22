package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Message;
import com.usatiuk.tjv.y.server.repository.MessageRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends CrudServiceImpl<Message, Long> implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    protected CrudRepository<Message, Long> getRepository() {
        return messageRepository;
    }
}

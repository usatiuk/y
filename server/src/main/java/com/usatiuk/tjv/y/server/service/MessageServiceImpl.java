package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.MessageCreateTo;
import com.usatiuk.tjv.y.server.dto.MessageTo;
import com.usatiuk.tjv.y.server.dto.converters.MessageMapper;
import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.entity.Message;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.MessageRepository;
import com.usatiuk.tjv.y.server.service.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final EntityManager entityManager;

    public MessageServiceImpl(MessageRepository messageRepository, MessageMapper messageMapper, EntityManager entityManager) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Collection<MessageTo> getByChat(Long chatId) {
        return messageRepository.findByChat_Id(chatId).stream().map(messageMapper::makeDto).toList();
    }

    @Override
    public MessageTo addToChat(Authentication authentication, Long chatId, MessageCreateTo messageCreateTo) {
        Message message = new Message().setChat(entityManager.getReference(Chat.class, chatId))
                .setAuthor(entityManager.getReference(Person.class, authentication.getName()))
                .setContents(messageCreateTo.contents());

        messageRepository.save(message);
        return messageMapper.makeDto(message);
    }

    @Override
    public MessageTo update(Long id, MessageCreateTo msg) {
        var message = messageRepository.findById(id).orElseThrow(NotFoundException::new);
        message.setContents(msg.contents());
        messageRepository.save(message);
        return messageMapper.makeDto(message);
    }

    @Override
    public void delete(Long id) {
        var read = messageRepository.findById(id);
        if (read.isEmpty()) return;
        messageRepository.delete(read.get());
    }

    @Override
    public Collection<MessageTo> readAll() {
        return StreamSupport.stream(messageRepository.findAll().spliterator(), false).map(messageMapper::makeDto).toList();
    }

    @Override
    public boolean isAuthorOf(String userUuid, Long messageId) {
        var msg = messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException("Chat not found"));
        return Objects.equals(msg.getAuthor().getUuid(), userUuid);
    }
}

package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.MessageCreateTo;
import com.usatiuk.tjv.y.server.dto.MessageTo;
import com.usatiuk.tjv.y.server.dto.converters.MessageMapper;
import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.entity.Message;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.MessageRepository;
import com.usatiuk.tjv.y.server.security.UserRoles;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ChatService chatService;
    private final EntityManager entityManager;

    public MessageServiceImpl(MessageRepository messageRepository, MessageMapper messageMapper, ChatService chatService, EntityManager entityManager) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.chatService = chatService;
        this.entityManager = entityManager;
    }

    @Override
    public Collection<MessageTo> getByChat(Authentication authentication, Long chatId) {
        if (!chatService.isMemberOf(authentication.getName(), chatId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't member of the chat");

        return messageRepository.findByChat_Id(chatId).stream().map(messageMapper::makeDto).toList();
    }

    @Override
    public MessageTo addToChat(Authentication authentication, Long chatId, MessageCreateTo messageCreateTo) {
        if (!chatService.isMemberOf(authentication.getName(), chatId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't member of the chat");

        Message message = new Message().setChat(entityManager.getReference(Chat.class, chatId))
                .setAuthor(entityManager.getReference(Person.class, authentication.getName()))
                .setContents(messageCreateTo.contents());

        messageRepository.save(message);
        return messageMapper.makeDto(message);
    }

    @Override
    public MessageTo update(Authentication authentication, Long id, MessageCreateTo msg) {
        var message = messageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!Objects.equals(message.getAuthor().getUuid(), authentication.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        message.setContents(msg.contents());
        messageRepository.save(message);
        return messageMapper.makeDto(message);
    }

    @Override
    public void delete(Authentication authentication, Long id) {
        var read = messageRepository.findById(id);
        if (read.isEmpty()) return;
        if (!Objects.equals(read.get().getAuthor().getId(), authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        messageRepository.delete(read.get());
    }

    @Override
    public Collection<MessageTo> readAll(Authentication authentication) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name())))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return StreamSupport.stream(messageRepository.findAll().spliterator(), false).map(messageMapper::makeDto).toList();
    }
}

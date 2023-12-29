package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.dto.converters.ChatMapper;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.ChatRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Service
public class ChatServiceImpl extends CrudServiceImpl<Chat, Long> implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final PersonMapper personMapper;
    private final EntityManager entityManager;

    public ChatServiceImpl(ChatRepository chatRepository, ChatMapper chatMapper, PersonMapper personMapper, EntityManager entityManager) {
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
        this.personMapper = personMapper;
        this.entityManager = entityManager;
    }

    @Override
    protected CrudRepository<Chat, Long> getRepository() {
        return chatRepository;
    }


    @Override
    public ChatTo create(Authentication authentication, ChatCreateTo chatCreateTo) {
        var chat = new Chat();

        if (Arrays.stream(chatCreateTo.memberUuids()).noneMatch(n -> Objects.equals(n, authentication.getName())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Creator of chat must be its member");

        if (chatCreateTo.memberUuids().length == 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chat must have members other than its creator");

        chat.setCreator(entityManager.getReference(Person.class, authentication.getName()));
        chat.setMembers(Arrays.stream(chatCreateTo.memberUuids()).map(
                p -> entityManager.getReference(Person.class, p)
        ).toList());
        chat.setName(chatCreateTo.name());

        chatRepository.save(chat);
        return chatMapper.makeDto(chat);
    }

    @Override
    public ChatTo update(Authentication authentication, Long id, ChatCreateTo chatCreateTo) {
        var chat = readById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        if (!Objects.equals(chat.getCreator().getUuid(), authentication.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't creator of the chat");

        if (Arrays.stream(chatCreateTo.memberUuids()).noneMatch(n -> Objects.equals(n, authentication.getName())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Creator of chat must be its member");

        if (chatCreateTo.memberUuids().length <= 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chat must have members other than its creator");

        chat.setMembers(new ArrayList<>(Arrays.stream(chatCreateTo.memberUuids()).map(
                p -> entityManager.getReference(Person.class, p)
        ).toList()));
        chat.setName(chatCreateTo.name());

        chatRepository.save(chat);
        return chatMapper.makeDto(chat);
    }

    @Override
    public Collection<ChatTo> getMy(Authentication authentication) {
        return chatRepository.findByMembers_uuid(authentication.getName()).stream().map(chatMapper::makeDto).toList();
    }

    @Override
    public ChatTo getById(Authentication authentication, Long id) {
        var chat = readById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        var userRef = entityManager.getReference(Person.class, authentication.getName());
        if (!chat.getMembers().contains(userRef))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't member of the chat");

        return chatMapper.makeDto(chat);
    }

    @Override
    public void deleteById(Authentication authentication, Long id) {
        var chat = readById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        if (!Objects.equals(chat.getCreator().getUuid(), authentication.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't creator of the chat");
        deleteById(id);
    }

    @Override
    public Collection<PersonTo> getMembers(Authentication authentication, Long id) {
        var chat = readById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        var userRef = entityManager.getReference(Person.class, authentication.getName());
        if (!chat.getMembers().contains(userRef))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't member of the chat");
        return chat.getMembers().stream().map(personMapper::makeDto).toList();
    }
}

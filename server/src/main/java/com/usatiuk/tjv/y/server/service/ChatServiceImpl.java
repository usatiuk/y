package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.dto.converters.ChatMapper;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.ChatRepository;
import com.usatiuk.tjv.y.server.service.exceptions.BadInputException;
import com.usatiuk.tjv.y.server.service.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Service("chatService")
@Transactional
public class ChatServiceImpl implements ChatService {

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
    public ChatTo create(Authentication authentication, ChatCreateTo chatCreateTo) {
        var chat = new Chat();

        if (Arrays.stream(chatCreateTo.memberUuids()).noneMatch(n -> Objects.equals(n, authentication.getName())))
            throw new BadInputException("Creator of chat must be its member");

        if (chatCreateTo.memberUuids().length == 1)
            throw new BadInputException("Chat must have members other than its creator");

        chat.setCreator(entityManager.getReference(Person.class, authentication.getName()));
        chat.setMembers(Arrays.stream(chatCreateTo.memberUuids()).map(
                p -> {
                    if (entityManager.find(Person.class, p) == null)
                        throw new NotFoundException("User with uuid " + p + " not found");
                    return entityManager.getReference(Person.class, p);
                }
        ).toList());
        chat.setName(chatCreateTo.name());

        chatRepository.save(chat);
        return chatMapper.makeDto(chat);
    }

    @Override
    public ChatTo update(Authentication authentication, Long id, ChatCreateTo chatCreateTo) {
        var chat = chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Chat not found"));

        if (Arrays.stream(chatCreateTo.memberUuids()).noneMatch(n -> Objects.equals(n, authentication.getName())))
            throw new BadInputException("Creator of chat must be its member");

        if (chatCreateTo.memberUuids().length <= 1)
            throw new BadInputException("Chat must have members other than its creator");

        chat.setMembers(new ArrayList<>(Arrays.stream(chatCreateTo.memberUuids()).map(
                p -> {
                    if (entityManager.find(Person.class, p) == null)
                        throw new NotFoundException("User with uuid " + p + " not found");
                    return entityManager.getReference(Person.class, p);
                }
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
    public ChatTo getById(Long id) {
        var chat = chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Chat not found"));
        return chatMapper.makeDto(chat);
    }

    @Override
    public void deleteById(Long id) {
        var chat = chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Chat not found"));
        chatRepository.delete(chat);
    }

    @Override
    public Collection<PersonTo> getMembers(Long id) {
        var chat = chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Chat not found"));
        return chat.getMembers().stream().map(personMapper::makeDto).toList();
    }

    @Override
    public boolean isMemberOf(String personUuid, Long chatId) {
        var chat = chatRepository.findById(chatId).orElseThrow(() -> new NotFoundException("Chat not found"));
        var userRef = entityManager.getReference(Person.class, personUuid);
        return chat.getMembers().contains(userRef);
    }

    @Override
    public boolean isCreatorOf(String personUuid, Long chatId) {
        var chat = chatRepository.findById(chatId).orElseThrow(() -> new NotFoundException("Chat not found"));
        return Objects.equals(chat.getCreator().getUuid(), personUuid);
    }
}

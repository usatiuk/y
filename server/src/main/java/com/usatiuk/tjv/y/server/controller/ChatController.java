package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.converters.ChatMapper;
import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.ChatService;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {
    private final EntityManager entityManager;
    private final ChatService chatService;
    private final ChatMapper chatMapper;

    public ChatController(EntityManager entityManager, ChatService chatService, ChatMapper chatMapper) {
        this.entityManager = entityManager;
        this.chatService = chatService;
        this.chatMapper = chatMapper;
    }

    @PostMapping
    public ChatTo create(Principal principal, @RequestBody ChatCreateTo chatCreateTo) {
        var chat = new Chat();

        if (Arrays.stream(chatCreateTo.memberUuids()).noneMatch(n -> Objects.equals(n, principal.getName())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Creator of chat must be its member");

        if (chatCreateTo.memberUuids().length <= 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chat must have members other than its creator");

        chat.setCreator(entityManager.getReference(Person.class, principal.getName()));
        chat.setMembers(Arrays.stream(chatCreateTo.memberUuids()).map(
                p -> entityManager.getReference(Person.class, p)
        ).toList());
        chat.setName(chatCreateTo.name());

        chatService.create(chat);
        return chatMapper.makeDto(chat);
    }

    @GetMapping(path = "/by-id/{id}")
    public ChatTo get(Principal principal, @PathVariable Long id) {
        var chat = chatService.readById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        var userRef = entityManager.getReference(Person.class, principal.getName());
        if (!chat.getMembers().contains(userRef))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't member of the chat");
        return chatMapper.makeDto(chat);
    }

    @DeleteMapping(path = "/by-id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Principal principal, @PathVariable Long id) {
        var chat = chatService.readById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        if (!Objects.equals(chat.getCreator().getUuid(), principal.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't creator of the chat");
        chatService.deleteById(id);
    }


    @GetMapping(path = "/my")
    public Stream<ChatTo> getMy(Principal principal) {
        return chatService.readByMember(principal.getName()).stream().map(chatMapper::makeDto);
    }


}
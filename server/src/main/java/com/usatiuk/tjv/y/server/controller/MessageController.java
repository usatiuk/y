package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.MessageCreateTo;
import com.usatiuk.tjv.y.server.dto.MessageTo;
import com.usatiuk.tjv.y.server.dto.converters.MessageMapper;
import com.usatiuk.tjv.y.server.entity.Message;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.ChatService;
import com.usatiuk.tjv.y.server.service.MessageService;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
    private final ChatService chatService;
    private final EntityManager entityManager;
    private final MessageMapper messageMapper;
    private final MessageService messageService;

    public MessageController(ChatService chatService, EntityManager entityManager, MessageMapper messageMapper, MessageService messageService) {
        this.chatService = chatService;
        this.entityManager = entityManager;
        this.messageMapper = messageMapper;
        this.messageService = messageService;
    }

    @GetMapping(path = "/by-chat/{chatTd}")
    public Stream<MessageTo> get(Principal principal, @PathVariable Long chatTd) {
        var chat = chatService.readById(chatTd).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        var userRef = entityManager.getReference(Person.class, principal.getName());
        if (!chat.getMembers().contains(userRef))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't member of the chat");

        return chat.getMessages().stream().map(messageMapper::makeDto);
    }

    @PostMapping(path = "/by-chat/{chatId}")
    public MessageTo post(Principal principal, @PathVariable Long chatId, @RequestBody MessageCreateTo messageCreateTo) {
        var chat = chatService.readById(chatId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        var userRef = entityManager.getReference(Person.class, principal.getName());
        if (!chat.getMembers().contains(userRef))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User isn't member of the chat");

        Message message = new Message().setChat(chat).setAuthor(userRef).setContents(messageCreateTo.contents());
        messageService.create(message);
        return messageMapper.makeDto(message);
    }
}

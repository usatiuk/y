package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.MessageCreateTo;
import com.usatiuk.tjv.y.server.dto.MessageTo;
import com.usatiuk.tjv.y.server.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(path = "/by-chat/{chatTd}")
    public Collection<MessageTo> get(Authentication authentication, @PathVariable Long chatTd) {
        return messageService.getByChat(authentication, chatTd);
    }

    @PostMapping(path = "/by-chat/{chatId}")
    public MessageTo post(Authentication authentication, @PathVariable Long chatId, @RequestBody MessageCreateTo messageCreateTo) {
        return messageService.addToChat(authentication, chatId, messageCreateTo);
    }

    @PatchMapping(path = "/by-id/{id}")
    public MessageTo update(Authentication authentication, @PathVariable long id, @RequestBody MessageCreateTo messageCreateTo) {
        return messageService.update(authentication, id, messageCreateTo);
    }

    @DeleteMapping(path = "/by-id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable long id) {
        messageService.delete(authentication, id);
    }

    @GetMapping
    public Collection<MessageTo> getAll(Authentication authentication) {
        return messageService.readAll(authentication);
    }


}

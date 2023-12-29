package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatTo create(Authentication authentication, @RequestBody ChatCreateTo chatCreateTo) {
        return chatService.create(authentication, chatCreateTo);
    }

    @GetMapping(path = "/by-id/{id}")
    public ChatTo get(Authentication authentication, @PathVariable Long id) {
        return chatService.getById(authentication, id);
    }

    @DeleteMapping(path = "/by-id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable Long id) {
        chatService.deleteById(authentication, id);
    }

    @PatchMapping(path = "/by-id/{id}")
    public ChatTo update(Authentication authentication, @PathVariable Long id, @RequestBody ChatCreateTo chatCreateTo) {
        return chatService.update(authentication, id, chatCreateTo);
    }
    
    @GetMapping(path = "/my")
    public Collection<ChatTo> getMy(Authentication authentication) {
        return chatService.getMy(authentication);
    }

    @GetMapping(path = "/by-id/{id}/members")
    public Collection<PersonTo> getMembers(Authentication authentication, @PathVariable Long id) {
        return chatService.getMembers(authentication, id);
    }


}

package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.controller.annotations.*;
import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Create a new chat, members must be at least the creator and someone else")
    @ApiUnauthorizedResponse
    @ApiBadRequestResponse
    @ChatToResponse
    public ChatTo create(Authentication authentication, @RequestBody ChatCreateTo chatCreateTo) {
        return chatService.create(authentication, chatCreateTo);
    }

    @GetMapping(path = "/by-id/{id}")
    @Operation(summary = "Get a chat by id, should be its member")
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    @ApiForbiddenResponse
    @ChatToResponse
    public ChatTo get(@PathVariable Long id) {
        return chatService.getById(id);
    }

    @DeleteMapping(path = "/by-id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a chat by id, should be its creator")
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    @ApiForbiddenResponse
    @ApiResponse
    public void delete(@PathVariable Long id) {
        chatService.deleteById(id);
    }

    @PatchMapping(path = "/by-id/{id}")
    @Operation(summary = "Update a chat by id, should be its creator, members must be at least the creator and someone else")
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    @ApiForbiddenResponse
    @ApiBadRequestResponse
    @ChatToResponse
    public ChatTo update(Authentication authentication, @PathVariable Long id, @RequestBody ChatCreateTo chatCreateTo) {
        return chatService.update(authentication, id, chatCreateTo);
    }

    @GetMapping(path = "/my")
    @ApiUnauthorizedResponse
    @ChatToArrResponse
    @Operation(summary = "Get chats token holder is member of")
    public Collection<ChatTo> getMy(Authentication authentication) {
        return chatService.getMy(authentication);
    }

    @GetMapping(path = "/by-id/{id}/members")
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    @ApiForbiddenResponse
    @ApiBadRequestResponse
    @PersonToArrResponse
    @Operation(summary = "Get members of chat by id, should be its member")
    public Collection<PersonTo> getMembers(@PathVariable Long id) {
        return chatService.getMembers(id);
    }


}

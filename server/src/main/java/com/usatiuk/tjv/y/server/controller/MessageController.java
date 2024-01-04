package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.controller.annotations.*;
import com.usatiuk.tjv.y.server.dto.MessageCreateTo;
import com.usatiuk.tjv.y.server.dto.MessageTo;
import com.usatiuk.tjv.y.server.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get messages in a chat, must be its member")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @ApiNotFoundResponse
    @MessageToArrResponse
    public Collection<MessageTo> get(@PathVariable Long chatTd) {
        return messageService.getByChat(chatTd);
    }

    @PostMapping(path = "/by-chat/{chatId}")
    @Operation(summary = "Add a message to a chat, must be its member")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @ApiNotFoundResponse
    @MessageToResponse
    @ApiBadRequestResponse
    public MessageTo post(Authentication authentication, @PathVariable Long chatId, @RequestBody MessageCreateTo messageCreateTo) {
        return messageService.addToChat(authentication, chatId, messageCreateTo);
    }

    @PatchMapping(path = "/by-id/{id}")
    @Operation(summary = "Change contents of a message, must be its author")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @ApiNotFoundResponse
    @MessageToResponse
    @ApiBadRequestResponse
    public MessageTo update(@PathVariable long id, @RequestBody MessageCreateTo messageCreateTo) {
        return messageService.update(id, messageCreateTo);
    }

    @DeleteMapping(path = "/by-id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a message, must be its author")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @ApiNotFoundResponse
    public void delete(@PathVariable long id) {
        messageService.delete(id);
    }

    @GetMapping
    @Operation(summary = "Get all messages, must be admin")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @MessageToArrResponse
    public Collection<MessageTo> getAll() {
        return messageService.readAll();
    }


}

package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.MessageCreateTo;
import com.usatiuk.tjv.y.server.dto.MessageTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public interface MessageService {
    @PreAuthorize("@chatService.isMemberOf(authentication.principal.username, #chatId)")
    Collection<MessageTo> getByChat(Long chatId);
    @PreAuthorize("@chatService.isMemberOf(authentication.principal.username, #chatId)")
    MessageTo addToChat(Authentication authentication, Long chatId, MessageCreateTo messageCreateTo);

    @PreAuthorize("@messageService.isAuthorOf(authentication.principal.username, #id)")
    MessageTo update(Long id, MessageCreateTo msg);
    @PreAuthorize("@messageService.isAuthorOf(authentication.principal.username, #id)")
    void delete(Long id);

    @Secured({"ROLE_ADMIN"})
    Collection<MessageTo> readAll();

    boolean isAuthorOf(String userUuid, Long messageId);
}

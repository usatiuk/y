package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.MessageCreateTo;
import com.usatiuk.tjv.y.server.dto.MessageTo;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public interface MessageService {
    Collection<MessageTo> getByChat(Authentication authentication, Long chatId);
    MessageTo addToChat(Authentication authentication, Long chatId, MessageCreateTo messageCreateTo);

    MessageTo update(Authentication authentication, Long id, MessageCreateTo msg);
    void delete(Authentication authentication, Long id);

    Collection<MessageTo> readAll(Authentication authentication);
}

package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.entity.Chat;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public interface ChatService extends CrudService<Chat, Long> {
    ChatTo create(Authentication authentication, ChatCreateTo chatCreateTo);
    ChatTo update(Authentication authentication, Long id, ChatCreateTo chatCreateTo);

    Collection<ChatTo> getMy(Authentication authentication);

    ChatTo getById(Authentication authentication, Long id);
    void deleteById(Authentication authentication, Long id);

    Collection<PersonTo> getMembers(Authentication authentication, Long id);
}

package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public interface ChatService {
    ChatTo create(Authentication authentication, ChatCreateTo chatCreateTo);
    ChatTo update(Authentication authentication, Long id, ChatCreateTo chatCreateTo);

    Collection<ChatTo> getMy(Authentication authentication);

    ChatTo getById(Authentication authentication, Long id);
    void deleteById(Authentication authentication, Long id);

    Collection<PersonTo> getMembers(Authentication authentication, Long id);

    boolean isMemberOf(String personUuid, Long chatId);
}

package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public interface ChatService {
    ChatTo create(Authentication authentication, ChatCreateTo chatCreateTo);

    @PreAuthorize("@chatService.isCreatorOf(authentication.principal.username, #id)")
    ChatTo update(Authentication authentication, Long id, ChatCreateTo chatCreateTo);

    Collection<ChatTo> getMy(Authentication authentication);

    @PreAuthorize("@chatService.isMemberOf(authentication.principal.username, #id)")
    ChatTo getById(Long id);
    @PreAuthorize("@chatService.isMemberOf(authentication.principal.username, #id)")
    void deleteById(Long id);

    @PreAuthorize("@chatService.isMemberOf(authentication.principal.username, #id)")
    Collection<PersonTo> getMembers(Long id);

    boolean isMemberOf(String personUuid, Long chatId);
    boolean isCreatorOf(String personUuid, Long chatId);
}

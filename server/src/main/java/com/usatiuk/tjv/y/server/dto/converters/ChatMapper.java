package com.usatiuk.tjv.y.server.dto.converters;

import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.entity.Chat;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {
    public ChatTo makeDto(Chat chat) {
        return new ChatTo(chat.getId(), chat.getName(), chat.getCreator().getUuid(), (long) chat.getMembers().size());
    }
}

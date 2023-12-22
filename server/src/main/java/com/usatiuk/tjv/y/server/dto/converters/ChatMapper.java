package com.usatiuk.tjv.y.server.dto.converters;

import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.entity.Chat;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    private final PersonMapper personMapper;
    private final MessageMapper messageMapper;

    public ChatMapper(PersonMapper personMapper, MessageMapper messageMapper) {
        this.personMapper = personMapper;
        this.messageMapper = messageMapper;
    }

    public ChatTo makeDto(Chat chat) {
        return new ChatTo(chat.getId(), chat.getName(), chat.getCreator().getUuid(),
                chat.getMembers().stream().map(personMapper::makeDto).toList(),
                chat.getMessages().stream().map(messageMapper::makeDto).toList());
    }
}

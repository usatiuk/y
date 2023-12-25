package com.usatiuk.tjv.y.server.dto.converters;

import com.usatiuk.tjv.y.server.dto.MessageTo;
import com.usatiuk.tjv.y.server.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public MessageTo makeDto(Message message) {
        return new MessageTo(message.getId(), message.getChat().getId(),
                message.getAuthor().getUuid(), message.getAuthor().getUsername(), message.getContents(), message.getCreatedAt().getEpochSecond());
    }
}

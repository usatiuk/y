package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.repository.ChatRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChatServiceImpl extends CrudServiceImpl<Chat, Long> implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    protected CrudRepository<Chat, Long> getRepository() {
        return chatRepository;
    }

    @Override
    public Collection<Chat> readByMember(String memberUuid) {
        return chatRepository.findByMembers_uuid(memberUuid);
    }
}

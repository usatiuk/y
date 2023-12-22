package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Chat;

import java.util.Collection;

public interface ChatService extends CrudService<Chat, Long> {
    Collection<Chat> readByMember(String memberUuid);
}

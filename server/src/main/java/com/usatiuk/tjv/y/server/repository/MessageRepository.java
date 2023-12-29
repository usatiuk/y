package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Collection<Message> findByChat_Id(Long chatId);
}

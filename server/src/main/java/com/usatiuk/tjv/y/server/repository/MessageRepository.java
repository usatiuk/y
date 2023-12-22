package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}

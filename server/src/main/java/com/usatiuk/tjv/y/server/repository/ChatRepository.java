package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Chat;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    Optional<Chat> findByName(String name);
}

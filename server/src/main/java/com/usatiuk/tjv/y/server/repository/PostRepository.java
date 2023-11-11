package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface PostRepository extends CrudRepository<Post, Long> {
    Collection<Post> findByAuthorUuid(String authorUuid);
}

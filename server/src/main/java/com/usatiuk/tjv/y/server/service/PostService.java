package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Post;

import java.util.Collection;

public interface PostService extends CrudService<Post, Long> {
    Collection<Post> readByAuthorId(String authorUuid);

    Collection<Post> readByPersonFollowees(String personUuid);
}

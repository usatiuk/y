package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.PostCreateTo;
import com.usatiuk.tjv.y.server.dto.PostTo;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public interface PostService {
    PostTo createPost(Authentication authentication, PostCreateTo postCreateTo);
    PostTo updatePost(Authentication authentication, Long id, PostCreateTo postCreateTo);
    void deletePost(Authentication authentication, Long id);

    PostTo readById(Long id);

    Collection<PostTo> readByAuthorId(String authorUuid);
    Collection<PostTo> readByAuthorUsername(String authorUsername);

    Collection<PostTo> readByPersonFollowees(Authentication authentication);

    Collection<PostTo> readAll(Authentication authentication);
}

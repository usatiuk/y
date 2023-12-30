package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.PostCreateTo;
import com.usatiuk.tjv.y.server.dto.PostTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public interface PostService {
    PostTo createPost(Authentication authentication, PostCreateTo postCreateTo);
    @PreAuthorize("@postService.isAuthorOf(authentication.principal.username, #id)")
    PostTo updatePost(Long id, PostCreateTo postCreateTo);
    @PreAuthorize("@postService.isAuthorOf(authentication.principal.username, #id)")
    void deletePost(Long id);

    PostTo readById(Long id);

    Collection<PostTo> readByAuthorId(String authorUuid);
    Collection<PostTo> readByAuthorUsername(String authorUsername);

    Collection<PostTo> readByPersonFollowees(Authentication authentication);

    @Secured({"ROLE_ADMIN"})
    Collection<PostTo> readAll();

    boolean isAuthorOf(String userUuid, Long postId);
}

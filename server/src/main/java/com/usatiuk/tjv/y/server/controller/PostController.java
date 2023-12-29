package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PostCreateTo;
import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.dto.converters.PostMapper;
import com.usatiuk.tjv.y.server.service.PostService;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {
    private final PostService postService;

    public PostController(PostService postService, PostMapper postMapper, EntityManager entityManager) {
        this.postService = postService;
    }

    @PostMapping
    public PostTo createPost(Authentication authentication, @RequestBody PostCreateTo postCreateTo) {
        return postService.createPost(authentication, postCreateTo);
    }

    @GetMapping(path = "/by-author-uuid/{uuid}")
    public Collection<PostTo> readAllByAuthorUuid(@PathVariable String uuid) {
        return postService.readByAuthorId(uuid);
    }

    @GetMapping(path = "/by-author-username/{username}")
    public Collection<PostTo> readAllByAuthorUsername(@PathVariable String username) {
        return postService.readByAuthorUsername(username);
    }

    @GetMapping(path = "/by-following")
    public Collection<PostTo> readAllByFollowees(Authentication authentication) {
        return postService.readByPersonFollowees(authentication);
    }

    @GetMapping(path = "/{id}")
    public PostTo get(@PathVariable long id) {
        return postService.readById(id);
    }

    @PatchMapping(path = "/{id}")
    public PostTo update(Authentication authentication, @PathVariable long id, @RequestBody PostCreateTo postCreateTo) {
        return postService.updatePost(authentication, id, postCreateTo);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable long id) {
        postService.deletePost(authentication, id);
    }

    @GetMapping
    public Collection<PostTo> getAll(Authentication authentication) {
        return postService.readAll(authentication);
    }

}

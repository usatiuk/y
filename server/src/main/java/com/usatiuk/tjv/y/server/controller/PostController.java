package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.controller.annotations.*;
import com.usatiuk.tjv.y.server.dto.PostCreateTo;
import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.dto.converters.PostMapper;
import com.usatiuk.tjv.y.server.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Create a post")
    @PostToResponse
    @ApiUnauthorizedResponse
    @ApiBadRequestResponse
    public PostTo createPost(Authentication authentication, @RequestBody PostCreateTo postCreateTo) {
        return postService.createPost(authentication, postCreateTo);
    }

    @GetMapping(path = "/by-author-uuid/{uuid}")
    @Operation(summary = "Read all posts by some author by their uuid")
    @PostToArrResponse
    @ApiNotFoundResponse
    @ApiUnauthorizedResponse
    public Collection<PostTo> readAllByAuthorUuid(@PathVariable String uuid) {
        return postService.readByAuthorId(uuid);
    }

    @GetMapping(path = "/by-author-username/{username}")
    @Operation(summary = "Read all posts by some author by their username")
    @PostToArrResponse
    @ApiNotFoundResponse
    @ApiUnauthorizedResponse
    public Collection<PostTo> readAllByAuthorUsername(@PathVariable String username) {
        return postService.readByAuthorUsername(username);
    }

    @GetMapping(path = "/by-following")
    @Operation(summary = "Read all posts by authors you're following")
    @PostToArrResponse
    @ApiUnauthorizedResponse
    public Collection<PostTo> readAllByFollowees(Authentication authentication) {
        return postService.readByPersonFollowees(authentication);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Read a post by id")
    @PostToResponse
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    public PostTo get(@PathVariable long id) {
        return postService.readById(id);
    }

    @PatchMapping(path = "/{id}")
    @Operation(summary = "Update a post, must be its author")
    @PostToResponse
    @ApiUnauthorizedResponse
    @ApiBadRequestResponse
    @ApiForbiddenResponse
    public PostTo update(@PathVariable long id, @RequestBody PostCreateTo postCreateTo) {
        return postService.updatePost(id, postCreateTo);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a post, must be its author")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    public void delete(@PathVariable long id) {
        postService.deletePost(id);
    }

    @GetMapping
    @Operation(summary = "Get all posts, must be admin")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @PostToArrResponse
    public Collection<PostTo> getAll() {
        return postService.readAll();
    }

}

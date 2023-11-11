package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PostCreate;
import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.entity.Post;
import com.usatiuk.tjv.y.server.service.PostService;
import jakarta.persistence.EntityManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {
    private final PostService postService;
    private final EntityManager entityManager;

    public PostController(PostService postService, EntityManager entityManager) {
        this.postService = postService;
        this.entityManager = entityManager;
    }

    @PostMapping
    public PostTo createPost(Principal principal, @RequestBody PostCreate postCreate) {
        Post post = new Post();
        post.setAuthor(entityManager.getReference(Person.class, principal.getName()));
        post.setText(postCreate.text());
        return new PostTo(postService.create(post));
    }

    @GetMapping
    public Iterable<Post> readAllOrByAuthor(@RequestParam Optional<String> author) {
        if (author.isPresent())
            return postService.readByAuthorId(author.get());
        else
            return postService.readAll();
    }

}

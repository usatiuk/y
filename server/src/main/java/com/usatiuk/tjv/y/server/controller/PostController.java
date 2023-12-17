package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PostCreateTo;
import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.dto.converters.PostMapper;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.entity.Post;
import com.usatiuk.tjv.y.server.service.PostService;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
    public PostTo createPost(Principal principal, @RequestBody PostCreateTo postCreateTo) {
        Post post = new Post();
        post.setAuthor(entityManager.getReference(Person.class, principal.getName()));
        post.setText(postCreateTo.text());
        return PostMapper.makeDto(postService.create(post));
    }

    @GetMapping(path = "/by-author-uuid")
    public Stream<PostTo> readAllByAuthorUuid(@RequestParam Optional<String> author) {
        if (author.isPresent())
            return postService.readByAuthorId(author.get()).stream().map(PostMapper::makeDto);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/by-author-username")
    public Stream<PostTo> readAllByAuthorUsername(@RequestParam Optional<String> author) {
        if (author.isPresent())
            return postService.readByAuthorId(author.get()).stream().map(PostMapper::makeDto);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/following")
    public Stream<PostTo> readAllByFollowees(Principal principal) {
        return postService.readByPersonFollowees(principal.getName()).stream().map(PostMapper::makeDto);
    }

    @GetMapping(path = "/{id}")
    public PostTo get(@PathVariable long id) {
        var post = postService.readById(id);
        if (post.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return PostMapper.makeDto(post.get());
    }


    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Principal principal, @PathVariable long id) {
        var read = postService.readById(id);
        if (read.isEmpty()) return;
        if (!Objects.equals(read.get().getAuthor().getId(), principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        postService.deleteById(id);
    }

}

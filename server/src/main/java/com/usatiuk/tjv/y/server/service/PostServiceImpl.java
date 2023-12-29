package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.PostCreateTo;
import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.dto.converters.PostMapper;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.entity.Post;
import com.usatiuk.tjv.y.server.repository.PostRepository;
import com.usatiuk.tjv.y.server.security.UserRoles;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final EntityManager entityManager;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, EntityManager entityManager) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.entityManager = entityManager;
    }

    @Override
    public PostTo createPost(Authentication authentication, PostCreateTo postCreateTo) {
        Post post = new Post();
        post.setAuthor(entityManager.getReference(Person.class, authentication.getName()));
        post.setText(postCreateTo.text());
        return postMapper.makeDto(postRepository.save(post));
    }

    @Override
    public PostTo readById(Long id) {
        return postMapper.makeDto(postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")));
    }

    @Override
    public Collection<PostTo> readByAuthorId(String authorId) {
        return postRepository.findByAuthorUuid(authorId).stream().map(postMapper::makeDto).toList();
    }

    @Override
    public Collection<PostTo> readByAuthorUsername(String authorUsername) {
        return postRepository.findByAuthorUsername(authorUsername).stream().map(postMapper::makeDto).toList();
    }

    @Override
    public Collection<PostTo> readByPersonFollowees(Authentication authentication) {
        return postRepository.findByPersonFollowees(authentication.getName()).stream().map(postMapper::makeDto).toList();
    }

    @Override
    public PostTo updatePost(Authentication authentication, Long id, PostCreateTo postCreateTo) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!Objects.equals(post.getAuthor().getUuid(), authentication.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        post.setText(postCreateTo.text());
        postRepository.save(post);
        return postMapper.makeDto(post);
    }

    @Override
    public void deletePost(Authentication authentication, Long id) {
        var read = postRepository.findById(id);
        if (read.isEmpty()) return;
        if (!Objects.equals(read.get().getAuthor().getId(), authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        postRepository.delete(read.get());
    }

    @Override
    public Collection<PostTo> readAll(Authentication authentication) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name())))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return StreamSupport.stream(postRepository.findAll().spliterator(), false).map(postMapper::makeDto).toList();
    }
}

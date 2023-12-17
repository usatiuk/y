package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Post;
import com.usatiuk.tjv.y.server.repository.PostRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PostServiceImpl extends CrudServiceImpl<Post, Long> implements PostService {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    protected CrudRepository<Post, Long> getRepository() {
        return postRepository;
    }

    @Override
    public Collection<Post> readByAuthorId(String authorId) {
        return postRepository.findByAuthorUuid(authorId);
    }

    @Override
    public Collection<Post> readByAuthorUsername(String authorUsername) {
        return postRepository.findByAuthorUsername(authorUsername);
    }

    @Override
    public Collection<Post> readByPersonFollowees(String personUuid) {
        return postRepository.findByPersonFollowees(personUuid);
    }
}

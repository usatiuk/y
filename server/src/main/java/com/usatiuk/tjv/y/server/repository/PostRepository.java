package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface PostRepository extends PagingAndSortingRepository<Post, Long>, CrudRepository<Post, Long> {
    Collection<Post> findByAuthorUuid(String authorUuid);

    Page<Post> findByAuthorUuid(String authorUuid, Pageable pageable);
}

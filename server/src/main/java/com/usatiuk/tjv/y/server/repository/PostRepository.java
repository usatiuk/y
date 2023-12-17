package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface PostRepository extends PagingAndSortingRepository<Post, Long>, CrudRepository<Post, Long> {
    Collection<Post> findByAuthorUuid(String authorUuid);

    Collection<Post> findByAuthorUsername(String authorUsername);

    @Query(value = "SELECT p FROM Post p " +
            "WHERE EXISTS " +
            "(SELECT u FROM Person u LEFT JOIN u.following f where u.uuid = :personUuid and f.uuid = p.author.uuid)")
    Collection<Post> findByPersonFollowees(String personUuid);
}

package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PostCreateTo;
import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.dto.converters.PostMapper;
import com.usatiuk.tjv.y.server.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class PostControllerTest extends DemoDataDbTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Test
    void shouldNotCreatePostWithoutAuth() {
        Long postsBefore = postRepository.count();
        var response = restTemplate.exchange(addr + "/post", HttpMethod.POST,
                new HttpEntity<>(new PostCreateTo("test text")), PostTo.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        Assertions.assertEquals(postRepository.count(), postsBefore);
    }

    @Test
    void shouldCreatePost() {
        var entity = new HttpEntity<>(new PostCreateTo("test text"), createAuthHeaders(person1Auth));

        var response = restTemplate.exchange(addr + "/post", HttpMethod.POST,
                entity, PostTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        PostTo reponsePostTo = response.getBody();
        Assertions.assertNotNull(reponsePostTo);

        Assertions.assertEquals(reponsePostTo.text(), "test text");
        Assertions.assertEquals(reponsePostTo.authorUuid(), person1.getUuid());
    }

    @Test
    void shouldGetPost() {
        var response = restTemplate.exchange(addr + "/post/" + post1.getId(), HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(person1Auth)), PostTo.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var parsedResponse = response.getBody();

        Assertions.assertNotNull(parsedResponse);
        Assertions.assertEquals(parsedResponse.text(), post1.getText());
        Assertions.assertEquals(parsedResponse.authorUuid(), person1.getUuid());
    }

    @Test
    void shouldGetByAuthor() {
        var response = restTemplate.exchange(addr + "/post/by-author-uuid/" + person1.getUuid(), HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(person1Auth)), PostTo[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var parsedResponse = response.getBody();
        var repoResponse = postRepository.findByAuthorUuid(person1.getUuid());

        Assertions.assertNotNull(parsedResponse);
        Assertions.assertTrue(parsedResponse.length > 0);
        Assertions.assertEquals(parsedResponse.length, repoResponse.size());
        Assertions.assertIterableEquals(Arrays.asList(parsedResponse), repoResponse.stream().map(postMapper::makeDto).toList());
    }

    @Test
    void shouldGetPostsByFollowees() {
        var response = restTemplate.exchange(addr + "/post/by-following",
                HttpMethod.GET, new HttpEntity<>(createAuthHeaders(person3Auth)), PostTo[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var parsedResponse = response.getBody();

        Assertions.assertNotNull(parsedResponse);
        Assertions.assertEquals(2, parsedResponse.length);
        Assertions.assertIterableEquals(Arrays.asList(parsedResponse), List.of(postMapper.makeDto(post1), postMapper.makeDto(post2)));
    }
}

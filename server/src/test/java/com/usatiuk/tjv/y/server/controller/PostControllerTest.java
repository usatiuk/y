package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PostCreate;
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

public class PostControllerTest extends DemoDataDbTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    void shouldNotCreatePostWithoutAuth() {
        Long postsBefore = postRepository.count();
        var response = restTemplate.exchange(addr + "/post", HttpMethod.POST,
                new HttpEntity<>(new PostCreate("test text")), PostTo.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        Assertions.assertEquals(postRepository.count(), postsBefore);
    }

    @Test
    void shouldCreatePost() {
        var entity = new HttpEntity<>(new PostCreate("test text"), createAuthHeaders(person1Auth));

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
                HttpEntity.EMPTY, PostTo.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var parsedResponse = response.getBody();

        Assertions.assertNotNull(parsedResponse);
        Assertions.assertEquals(parsedResponse.text(), post1.getText());
        Assertions.assertEquals(parsedResponse.authorUuid(), person1.getUuid());
    }

    @Test
    void shouldGetByAuthor() {
        var response = restTemplate.exchange(addr + "/post?author=" + person1.getUuid(), HttpMethod.GET,
                HttpEntity.EMPTY, PostTo[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var parsedResponse = response.getBody();
        var repoResponse = postRepository.findByAuthorUuid(person1.getUuid());

        Assertions.assertNotNull(parsedResponse);
        Assertions.assertTrue(parsedResponse.length > 0);
        Assertions.assertEquals(parsedResponse.length, repoResponse.size());
        Assertions.assertIterableEquals(Arrays.asList(parsedResponse), repoResponse.stream().map(PostMapper::makeDto).toList());
    }

}

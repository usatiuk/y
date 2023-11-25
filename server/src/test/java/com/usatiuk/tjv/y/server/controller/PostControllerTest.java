package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PostCreate;
import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class PostControllerTest extends DemoDataDbTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    void shouldNotCreatePostWithoutAuth() {
        Long postsBefore = postRepository.count();
        var response = restTemplate.postForObject(addr + "/post",
                new PostCreate("test text"), PostTo.class);

        Assertions.assertNull(response);
        Assertions.assertEquals(postRepository.count(), postsBefore);
    }

    @Test
    void shouldCreatePost() {
        var entity = new HttpEntity<>(new PostCreate("test text"), createAuthHeaders(person1Auth));

        var response = restTemplate.exchange(addr + "/post", HttpMethod.POST,
                entity, PostTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        PostTo reponsePostTo = response.getBody();
        Assertions.assertNotNull(reponsePostTo);

        Assertions.assertEquals(reponsePostTo.text(), "test text");
        Assertions.assertEquals(reponsePostTo.authorUuid(), person1Auth.uuid());
    }

    @Test
    void shouldGetPost() {
        PostTo response = restTemplate.getForObject(addr + "/post/" + post1.getId(), PostTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.text(), post1.getText());
        Assertions.assertEquals(response.authorUuid(), person1Auth.uuid());
    }

}

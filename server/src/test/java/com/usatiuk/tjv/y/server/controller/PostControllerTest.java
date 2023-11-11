package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonAuthResponse;
import com.usatiuk.tjv.y.server.dto.PersonSignupRequest;
import com.usatiuk.tjv.y.server.dto.PostCreate;
import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostService postService;

    private String addr;
    private PersonAuthResponse personAuth;

    @BeforeEach
    void beforeEach() {
        addr = "http://localhost:" + port;

        // Signup a user
        var response = restTemplate.postForObject(addr + "/person",
                new PersonSignupRequest("name", "full name", "pass"), PersonAuthResponse.class);

        personAuth = response;
    }

    @Test
    void shouldNotCreatePostWithoutAuth() {
        var response = restTemplate.postForObject(addr + "/post",
                new PostCreate("test text"), PostTo.class);

        Assertions.assertNull(response);
        Assertions.assertEquals(postService.readAll().spliterator().estimateSize(), 0);
    }

    @Test
    void shouldCreatePost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + personAuth.token());
        var entity = new HttpEntity<>(new PostCreate("test text"), headers);

        var response = restTemplate.exchange(addr + "/post", HttpMethod.POST,
                entity, PostTo.class);

        PostTo reponsePostTo = response.getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(reponsePostTo.text(), "test text");
        Assertions.assertEquals(reponsePostTo.authorUuid(), personAuth.uuid());
    }
}

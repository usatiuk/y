package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.TokenResponse;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.entity.Post;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import com.usatiuk.tjv.y.server.repository.PostRepository;
import com.usatiuk.tjv.y.server.service.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class DemoDataDbTest {
    @Value("http://localhost:${local.server.port}")
    protected String addr;
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PostRepository postRepository;

    protected static final String person1Password = "p1p";
    protected Person person1;
    protected TokenResponse person1Auth;
    protected static final String person2Password = "p2p";
    protected Person person2;
    protected TokenResponse person2Auth;
    protected static final String person3Password = "p3p";
    protected Person person3;
    protected TokenResponse person3Auth;

    protected Post post1;
    protected Post post2;

    protected HttpHeaders createAuthHeaders(TokenResponse personAuth) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + personAuth.token());
        return headers;
    }

    @BeforeEach
    void setup() {
        assert !TestTransaction.isActive();
        person1 = personRepository.save(
                new Person()
                        .setUsername("person1")
                        .setFullName("Person 1")
                        .setPassword(passwordEncoder.encode(person1Password)));
        person1Auth = new TokenResponse(tokenService.generateToken(person1.getUuid()));
        person2 = personRepository.save(
                new Person()
                        .setUsername("person2")
                        .setFullName("Person 2")
                        .setPassword(passwordEncoder.encode(person2Password)));
        person2Auth = new TokenResponse(tokenService.generateToken(person2.getUuid()));
        person3 = personRepository.save(
                new Person()
                        .setUsername("person3")
                        .setFullName("Person 3")
                        .setPassword(passwordEncoder.encode(person3Password)));
        person3Auth = new TokenResponse(tokenService.generateToken(person3.getUuid()));

        post1 = postRepository.save(new Post().setAuthor(person1).setText("post 1"));
        post2 = postRepository.save(new Post().setAuthor(person2).setText("post 2"));
    }

    @AfterEach
    void erase() {
        assert !TestTransaction.isActive();
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "post", "person");
    }

}

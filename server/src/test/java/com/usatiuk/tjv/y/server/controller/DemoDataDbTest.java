package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonAuthResponse;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.entity.Post;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import com.usatiuk.tjv.y.server.repository.PostRepository;
import com.usatiuk.tjv.y.server.service.PersonTokenService;
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
    private PersonTokenService personTokenService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PostRepository postRepository;

    protected Person person1;
    protected PersonAuthResponse person1Auth;
    protected Person person2;
    protected PersonAuthResponse person2Auth;

    protected Post post1;
    protected Post post2;

    protected HttpHeaders createAuthHeaders(PersonAuthResponse personAuth) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + personAuth.token());
        return headers;
    }

    @BeforeEach
    void setup() {
        person1 = personRepository.save(
                new Person()
                        .setUsername("person1")
                        .setFullName("Person 1")
                        .setPassword(passwordEncoder.encode("p1p")));
        person1Auth = new PersonAuthResponse(person1, personTokenService.generateToken(person1.getUuid()));
        person2 = personRepository.save(
                new Person()
                        .setUsername("person2")
                        .setFullName("Person 2")
                        .setPassword(passwordEncoder.encode("p2p")));
        person2Auth = new PersonAuthResponse(person1, personTokenService.generateToken(person1.getUuid()));

        post1 = postRepository.save(new Post().setAuthor(person1).setText("post 1"));
        post2 = postRepository.save(new Post().setAuthor(person2).setText("post 2"));
    }

    @AfterEach
    void erase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "post", "person");
    }

}

package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.TokenResponseTo;
import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.entity.Message;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.entity.Post;
import com.usatiuk.tjv.y.server.repository.ChatRepository;
import com.usatiuk.tjv.y.server.repository.MessageRepository;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import com.usatiuk.tjv.y.server.repository.PostRepository;
import com.usatiuk.tjv.y.server.security.JwtTokenService;
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
import java.util.List;

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
    private JwtTokenService jwtTokenService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;

    protected static final String person1Password = "p1p";
    protected Person person1;
    protected TokenResponseTo person1Auth;
    protected static final String person2Password = "p2p";
    protected Person person2;
    protected TokenResponseTo person2Auth;
    protected static final String person3Password = "p3p";
    protected Person person3;
    protected TokenResponseTo person3Auth;

    protected Post post1;
    protected Post post2;

    protected Chat chat1;
    protected Chat chat2;

    protected Message message1;
    protected Message message2;
    protected Message message3;
    protected Message message4;

    protected HttpHeaders createAuthHeaders(TokenResponseTo personAuth) {
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
        person1Auth = new TokenResponseTo(jwtTokenService.generateToken(person1.getUuid()));
        person2 = personRepository.save(
                new Person()
                        .setUsername("person2")
                        .setFullName("Person 2")
                        .setPassword(passwordEncoder.encode(person2Password)).setFollowing(List.of(person1)));
        person2Auth = new TokenResponseTo(jwtTokenService.generateToken(person2.getUuid()));
        person3 = personRepository.save(
                new Person()
                        .setUsername("person3")
                        .setFullName("Person 3")
                        .setPassword(passwordEncoder.encode(person3Password))
                        .setFollowing(List.of(person2, person1)));
        person3Auth = new TokenResponseTo(jwtTokenService.generateToken(person3.getUuid()));

        post1 = postRepository.save(new Post().setAuthor(person1).setText("post 1"));
        post2 = postRepository.save(new Post().setAuthor(person2).setText("post 2"));

        chat1 = chatRepository.save(
                new Chat()
                        .setCreator(person1)
                        .setMembers(List.of(person1, person2))
                        .setName("Chat 1"));
        chat2 = chatRepository.save(
                new Chat()
                        .setCreator(person3)
                        .setMembers(List.of(person2, person3))
                        .setName("Chat 1"));

        message1 = messageRepository.save(new Message().setAuthor(person1).setChat(chat1).setContents("message 1"));
        message2 = messageRepository.save(new Message().setAuthor(person2).setChat(chat1).setContents("message2"));
        message3 = messageRepository.save(new Message().setAuthor(person2).setChat(chat2).setContents("message 3"));
        message4 = messageRepository.save(new Message().setAuthor(person3).setChat(chat2).setContents("message 4"));
    }

    @AfterEach
    void erase() {
        assert !TestTransaction.isActive();
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "person_follows", "chat_person", "post", "message", "chat", "person");
    }

}

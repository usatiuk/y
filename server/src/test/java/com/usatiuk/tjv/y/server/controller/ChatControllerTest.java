package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.ChatCreateTo;
import com.usatiuk.tjv.y.server.dto.ChatTo;
import com.usatiuk.tjv.y.server.dto.ErrorTo;
import com.usatiuk.tjv.y.server.dto.TokenResponseTo;
import com.usatiuk.tjv.y.server.dto.converters.ChatMapper;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.repository.ChatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Stream;

public class ChatControllerTest extends DemoDataDbTest {

    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    void shouldCreateChat() {
        var response = restTemplate.exchange(addr + "/chat", HttpMethod.POST,
                new HttpEntity<>(new ChatCreateTo("chatnew", new String[]{person1.getUuid(), person2.getUuid()}), createAuthHeaders(person1Auth)),
                ChatTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        var toResponse = response.getBody();
        Assertions.assertNotNull(toResponse);

        Assertions.assertEquals("chatnew", toResponse.name());
        Assertions.assertEquals(person1.getUuid(), toResponse.creatorUuid());
        Assertions.assertIterableEquals(Stream.of(person1, person2).map(personMapper::makeDto).toList(), toResponse.members());
        Assertions.assertTrue(chatRepository.findByName("chatnew").isPresent());
    }

    @Test
    void shouldGetChat() {
        for (TokenResponseTo t : new TokenResponseTo[]{person1Auth, person2Auth}) {
            var response = restTemplate.exchange(addr + "/chat/by-id/" + chat1.getId(), HttpMethod.GET,
                    new HttpEntity<>(createAuthHeaders(t)),
                    ChatTo.class);

            Assertions.assertNotNull(response);
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

            var toResponse = response.getBody();
            Assertions.assertNotNull(toResponse);

            Assertions.assertEquals(chatMapper.makeDto(chat1), toResponse);
        }
    }

    @Test
    void shouldGetChatsUserIsMemberOf() {
        var response = restTemplate.exchange(addr + "/chat/my", HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(person2Auth)),
                ChatTo[].class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        var toResponse = response.getBody();
        Assertions.assertNotNull(toResponse);

        Assertions.assertIterableEquals(Stream.of(chat1, chat2).map(chatMapper::makeDto).toList(), Arrays.asList(toResponse));
    }

    @Test
    void shouldDeleteChat() {
        Long chat1Id = chat1.getId();
        var response = restTemplate.exchange(addr + "/chat/by-id/" + chat1Id, HttpMethod.DELETE,
                new HttpEntity<>(createAuthHeaders(person1Auth)),
                Object.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Assertions.assertFalse(chatRepository.existsById(chat1Id));
    }

    @Test
    void shouldNotDeleteChatUnauthorized() {
        Long chatId = chat2.getId();
        var response = restTemplate.exchange(addr + "/chat/by-id/" + chatId, HttpMethod.DELETE,
                new HttpEntity<>(createAuthHeaders(person1Auth)),
                ErrorTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        var toResponse = response.getBody();
        Assertions.assertNotNull(toResponse);

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), toResponse.code());

        Assertions.assertTrue(chatRepository.existsById(chatId));
    }


    @Test
    void shouldNotChatUnauthorized() {
        var response = restTemplate.exchange(addr + "/chat/by-id/" + chat1.getId(), HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(person3Auth)),
                ErrorTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        var toResponse = response.getBody();
        Assertions.assertNotNull(toResponse);

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), toResponse.code());

        response = restTemplate.exchange(addr + "/chat/by-id/" + chat1.getId(), HttpMethod.GET,
                HttpEntity.EMPTY,
                ErrorTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        toResponse = response.getBody();
        Assertions.assertNotNull(toResponse);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), toResponse.code());
    }

}

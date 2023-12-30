package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.ErrorTo;
import com.usatiuk.tjv.y.server.dto.MessageTo;
import com.usatiuk.tjv.y.server.repository.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class MessageControllerTest extends DemoDataDbTest {

    @Autowired
    MessageRepository messageRepository;

    @Test
    void shouldGetMessagesIfAdmin() {
        var response = restTemplate.exchange(addr + "/message", HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(person1Auth)),
                MessageTo[].class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        var toResponse = response.getBody();
        Assertions.assertNotNull(toResponse);

        Assertions.assertEquals(toResponse.length, messageRepository.findAll().spliterator().estimateSize());
    }

    @Test
    void shouldNotGetMessagesIfNotAdmin() {
        var response = restTemplate.exchange(addr + "/message", HttpMethod.GET,
                new HttpEntity<>(createAuthHeaders(person2Auth)),
                ErrorTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        var toResponse = response.getBody();
        Assertions.assertNotNull(toResponse);
        Assertions.assertEquals(toResponse.code(), HttpStatus.FORBIDDEN.value());
    }

}

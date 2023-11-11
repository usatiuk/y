package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonAuthResponse;
import com.usatiuk.tjv.y.server.dto.PersonSignupRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String addr;

    @BeforeEach
    void beforeEach() {
        addr = "http://localhost:" + port;
    }

    @Test
    void shouldSignUp() {
        var response = restTemplate.postForObject(addr + "/person",
                new PersonSignupRequest("name", "full name", "pass"), PersonAuthResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.username(), "name");
        Assertions.assertEquals(response.fullName(), "full name");
    }
}

package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonAuthResponse;
import com.usatiuk.tjv.y.server.dto.PersonSignupRequest;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;

public class PersonControllerTest extends DemoDataDbTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void shouldSignUp() {
        var response = restTemplate.postForObject(addr + "/person",
                new PersonSignupRequest("usernew", "full name", "pass"), PersonAuthResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.username(), "usernew");
        Assertions.assertEquals(response.fullName(), "full name");
        Assertions.assertTrue(personRepository.findByUsername("usernew").isPresent());
    }
}

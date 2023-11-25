package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonSignupRequest;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class PersonControllerTest extends DemoDataDbTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void shouldSignUp() {
        var response = restTemplate.exchange(addr + "/person", HttpMethod.POST,
                new HttpEntity<>(new PersonSignupRequest("usernew", "full name", "pass")),
                PersonTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PersonTo personToResponse = response.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(personToResponse.username(), "usernew");
        Assertions.assertEquals(personToResponse.fullName(), "full name");
        Assertions.assertTrue(personRepository.findByUsername("usernew").isPresent());
    }

    @Test
    void shouldGet() {
        var response = restTemplate.exchange(addr + "/person/" + person1.getUsername(),
                HttpMethod.GET, HttpEntity.EMPTY, PersonTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PersonTo personToResponse = response.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(personToResponse.username(), person1.getUsername());
        Assertions.assertEquals(personToResponse.fullName(), person1.getFullName());
    }

}

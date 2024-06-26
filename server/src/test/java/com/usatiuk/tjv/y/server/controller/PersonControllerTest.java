package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonCreateTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class PersonControllerTest extends DemoDataDbTest {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Test
    void shouldSignUp() {
        var response = restTemplate.exchange(addr + "/person", HttpMethod.POST,
                new HttpEntity<>(new PersonCreateTo("usernew", "full name", "pass")),
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
        var response = restTemplate.exchange(addr + "/person/by-username/" + person1.getUsername(),
                HttpMethod.GET, new HttpEntity<>(createAuthHeaders(person1Auth)), PersonTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PersonTo personToResponse = response.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(personToResponse.username(), person1.getUsername());
        Assertions.assertEquals(personToResponse.fullName(), person1.getFullName());
    }

    @Test
    void shouldGetSelf() {
        var response = restTemplate.exchange(addr + "/person/self",
                HttpMethod.GET, new HttpEntity<>(createAuthHeaders(person1Auth)), PersonTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PersonTo personToResponse = response.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(personToResponse.username(), person1.getUsername());
        Assertions.assertEquals(personToResponse.fullName(), person1.getFullName());
    }

    @Test
    void shouldDeleteSelf() {
        var response = restTemplate.exchange(addr + "/person/self",
                HttpMethod.DELETE, new HttpEntity<>(createAuthHeaders(person2Auth)), Object.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Assertions.assertFalse(personRepository.existsById(person2.getUuid()));
    }

    @Test
    void shouldGetFollowers() {
        var response = restTemplate.exchange(addr + "/person/followers",
                HttpMethod.GET, new HttpEntity<>(createAuthHeaders(person2Auth)), PersonTo[].class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PersonTo[] personToResponse = response.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(1, personToResponse.length);
        Assertions.assertEquals(personToResponse[0].fullName(), person3.getFullName());

        response = restTemplate.exchange(addr + "/person/followers",
                HttpMethod.GET, new HttpEntity<>(createAuthHeaders(person1Auth)), PersonTo[].class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        personToResponse = response.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(2, personToResponse.length);
        Assertions.assertIterableEquals(Arrays.asList(personToResponse), List.of(personMapper.makeDto(person2), personMapper.makeDto(person3)));
    }

    @Test
    void shouldGetFollowees() {
        var response = restTemplate.exchange(addr + "/person/following",
                HttpMethod.GET, new HttpEntity<>(createAuthHeaders(person2Auth)), PersonTo[].class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PersonTo[] personToResponse = response.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(1, personToResponse.length);
        Assertions.assertEquals(personToResponse[0].fullName(), person1.getFullName());

        response = restTemplate.exchange(addr + "/person/following",
                HttpMethod.GET, new HttpEntity<>(createAuthHeaders(person3Auth)), PersonTo[].class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        personToResponse = response.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(2, personToResponse.length);
        Assertions.assertIterableEquals(Arrays.asList(personToResponse), List.of(personMapper.makeDto(person2), personMapper.makeDto(person1)));
    }

    @Test
    void shouldAddFollowing() {
        var response = restTemplate.exchange(addr + "/person/following/" + person3.getUuid(),
                HttpMethod.PUT, new HttpEntity<>(createAuthHeaders(person1Auth)), Object.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        var response2 = restTemplate.exchange(addr + "/person/following",
                HttpMethod.GET, new HttpEntity<>(createAuthHeaders(person1Auth)), PersonTo[].class);

        Assertions.assertNotNull(response2);
        Assertions.assertEquals(HttpStatus.OK, response2.getStatusCode());

        var personToResponse = response2.getBody();
        Assertions.assertNotNull(personToResponse);

        Assertions.assertEquals(1, personToResponse.length);
        Assertions.assertIterableEquals(Arrays.asList(personToResponse), List.of(personMapper.makeDto(person3)));
    }

}

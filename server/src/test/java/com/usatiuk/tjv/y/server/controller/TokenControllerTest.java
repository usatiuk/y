package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.TokenRequestTo;
import com.usatiuk.tjv.y.server.dto.TokenResponseTo;
import com.usatiuk.tjv.y.server.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class TokenControllerTest extends DemoDataDbTest {

    @Autowired
    private TokenService tokenService;

    @Test
    void shouldLogin() {
        var response = restTemplate.exchange(addr + "/token", HttpMethod.POST,
                new HttpEntity<>(new TokenRequestTo(person1.getUsername(), person1Password)), TokenResponseTo.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        TokenResponseTo parsedResponse = response.getBody();
        Assertions.assertNotNull(parsedResponse);

        Assertions.assertTrue(tokenService.parseToken(parsedResponse.token()).isPresent());
    }


}

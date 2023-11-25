package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.TokenRequest;
import com.usatiuk.tjv.y.server.dto.TokenResponse;
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
                new HttpEntity<>(new TokenRequest(person1.getUsername(), person1Password)), TokenResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        TokenResponse parsedResponse = response.getBody();
        Assertions.assertNotNull(parsedResponse);

        Assertions.assertTrue(tokenService.parseToken(parsedResponse.token()).isPresent());
    }


}

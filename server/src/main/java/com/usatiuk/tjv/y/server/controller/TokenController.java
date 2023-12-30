package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.TokenRequestTo;
import com.usatiuk.tjv.y.server.dto.TokenResponseTo;
import com.usatiuk.tjv.y.server.service.LoginTokenService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {
    private final LoginTokenService loginTokenService;

    public TokenController(LoginTokenService loginTokenService) {
        this.loginTokenService = loginTokenService;
    }

    @PostMapping
    public TokenResponseTo request(@RequestBody TokenRequestTo tokenRequestTo) {
        return loginTokenService.login(tokenRequestTo);
    }

}

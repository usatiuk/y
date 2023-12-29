package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.TokenRequestTo;
import com.usatiuk.tjv.y.server.dto.TokenResponseTo;
import com.usatiuk.tjv.y.server.service.LoginTokenService;
import com.usatiuk.tjv.y.server.service.PersonService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {
    private final PersonService personService;
    private final LoginTokenService loginTokenService;

    public TokenController(PersonService personService, LoginTokenService loginTokenService) {
        this.personService = personService;
        this.loginTokenService = loginTokenService;
    }

    @PostMapping
    public TokenResponseTo request(@RequestBody TokenRequestTo tokenRequestTo) {
        return loginTokenService.login(tokenRequestTo);
    }

}

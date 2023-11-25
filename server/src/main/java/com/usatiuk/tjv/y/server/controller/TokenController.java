package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.TokenRequest;
import com.usatiuk.tjv.y.server.dto.TokenResponse;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.PersonService;
import com.usatiuk.tjv.y.server.service.TokenService;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {
    private final PersonService personService;
    private final TokenService tokenService;

    public TokenController(PersonService personService, TokenService tokenService) {
        this.personService = personService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public TokenResponse request(@RequestBody TokenRequest tokenRequest) throws UserNotFoundException {
        Optional<Person> found = personService.login(tokenRequest.username(), tokenRequest.password());

        if (found.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return new TokenResponse(tokenService.generateToken(found.get().getId()));
    }

}

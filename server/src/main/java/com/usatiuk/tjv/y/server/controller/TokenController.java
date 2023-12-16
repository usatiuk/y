package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.TokenRequestTo;
import com.usatiuk.tjv.y.server.dto.TokenResponseTo;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.PersonService;
import com.usatiuk.tjv.y.server.service.TokenService;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
    public TokenResponseTo request(@RequestBody TokenRequestTo tokenRequestTo) throws UserNotFoundException {
        Optional<Person> found = personService.login(tokenRequestTo.username(), tokenRequestTo.password());

        if (found.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find user");

        return new TokenResponseTo(tokenService.generateToken(found.get().getId()));
    }

}

package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonAuthResponse;
import com.usatiuk.tjv.y.server.dto.PersonLoginRequest;
import com.usatiuk.tjv.y.server.dto.PersonSignupRequest;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.PersonService;
import com.usatiuk.tjv.y.server.service.PersonTokenService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {
    private final PersonService personService;
    private final PersonTokenService personTokenService;

    public PersonController(PersonService personService, PersonTokenService personTokenService) {
        this.personService = personService;
        this.personTokenService = personTokenService;
    }

    @PostMapping
    public PersonAuthResponse signup(@RequestBody PersonSignupRequest signupRequest) {
        Person toCreate = new Person();
        toCreate.setUsername(signupRequest.username())
                .setPassword(signupRequest.password())
                .setFullName(signupRequest.fullName());

        Person created = personService.signup(toCreate);

        return new PersonAuthResponse(created, personTokenService.generateToken(created.getId()));
    }
}

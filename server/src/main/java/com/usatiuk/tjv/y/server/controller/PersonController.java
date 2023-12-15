package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.dto.PersonSignupRequest;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.PersonService;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public PersonTo signup(@RequestBody PersonSignupRequest signupRequest) throws UserAlreadyExistsException {
        Person toCreate = new Person();
        toCreate.setUsername(signupRequest.username())
                .setPassword(signupRequest.password())
                .setFullName(signupRequest.fullName());

        Person created = personService.signup(toCreate);

        return PersonMapper.makeDto(created);
    }

    @GetMapping(path = "/{username}")
    public PersonTo get(@PathVariable String username) throws UserNotFoundException {
        Optional<Person> found = personService.readByUsername(username);

        if (found.isEmpty()) throw new UserNotFoundException();

        return PersonMapper.makeDto(found.get());
    }

    @GetMapping(path = "/followers")
    public Stream<PersonTo> getFollowers(Principal principal) throws UserNotFoundException {
        return personService.getFollowers(principal.getName()).stream().map(PersonMapper::makeDto);
    }

    @GetMapping(path = "/following")
    public Stream<PersonTo> getFollowing(Principal principal) throws UserNotFoundException {
        return personService.getFollowing(principal.getName()).stream().map(PersonMapper::makeDto);
    }

}

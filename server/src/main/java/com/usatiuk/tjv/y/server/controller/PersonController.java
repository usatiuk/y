package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonSignupTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.ChatService;
import com.usatiuk.tjv.y.server.service.PersonService;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {
    private final PersonService personService;
    private final ChatService chatService;
    private final PersonMapper personMapper;
    private final PasswordEncoder passwordEncoder;

    public PersonController(PersonService personService, ChatService chatService, PersonMapper personMapper, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.chatService = chatService;
        this.personMapper = personMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public PersonTo signup(@RequestBody PersonSignupTo signupRequest) throws UserAlreadyExistsException {
        Person toCreate = new Person();
        toCreate.setUsername(signupRequest.username())
                .setPassword(signupRequest.password())
                .setFullName(signupRequest.fullName());

        Person created = personService.signup(toCreate);

        return personMapper.makeDto(created);
    }

    @GetMapping(path = "/by-username/{username}")
    public PersonTo getByUsername(@PathVariable String username) throws UserNotFoundException {
        Optional<Person> found = personService.readByUsername(username);

        if (found.isEmpty()) throw new UserNotFoundException();

        return personMapper.makeDto(found.get());
    }

    @GetMapping(path = "/by-uuid/{uuid}")
    public PersonTo getByUuid(@PathVariable String uuid) throws UserNotFoundException {
        Optional<Person> found = personService.readById(uuid);

        if (found.isEmpty()) throw new UserNotFoundException();

        return personMapper.makeDto(found.get());
    }


    @GetMapping(path = "/self")
    public PersonTo getSelf(Principal principal) throws UserNotFoundException {
        Optional<Person> found = personService.readById(principal.getName());

        if (found.isEmpty()) throw new UserNotFoundException();

        return personMapper.makeDto(found.get());
    }

    @PatchMapping(path = "/self")
    public PersonTo update(Principal principal, @RequestBody PersonSignupTo personSignupTo) {
        var person = personService.readById(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        person.setUsername(personSignupTo.username())
                .setFullName(personSignupTo.fullName());
        if (!personSignupTo.password().isEmpty()) person.setPassword(passwordEncoder.encode(personSignupTo.password()));
        personService.update(person);
        return personMapper.makeDto(person);
    }

    @DeleteMapping(path = "/self")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Principal principal) {
        var person = personService.readById(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        for (Chat c : person.getChats()) {
            c.getMembers().remove(person);
            chatService.update(c);
        }
        personService.deleteById(principal.getName());
    }

    @GetMapping
    public Stream<PersonTo> getAll() throws UserNotFoundException {
        return StreamSupport.stream(personService.readAll().spliterator(), false).map(personMapper::makeDto);
    }

    @GetMapping(path = "/followers")
    public Stream<PersonTo> getFollowers(Principal principal) throws UserNotFoundException {
        return personService.getFollowers(principal.getName()).stream().map(personMapper::makeDto);
    }

    @GetMapping(path = "/following")
    public Stream<PersonTo> getFollowing(Principal principal) throws UserNotFoundException {
        return personService.getFollowing(principal.getName()).stream().map(personMapper::makeDto);
    }

    @PutMapping(path = "/following/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFollowing(Principal principal, @PathVariable String uuid) throws UserNotFoundException {
        personService.addFollower(principal.getName(), uuid);
    }

    @DeleteMapping(path = "/following/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFollowing(Principal principal, @PathVariable String uuid) throws UserNotFoundException {
        personService.removeFollower(principal.getName(), uuid);
    }

}

package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.PersonCreateTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.service.PersonService;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public PersonTo signup(@RequestBody PersonCreateTo signupRequest) throws UserAlreadyExistsException {
        return personService.signup(signupRequest);
    }

    @GetMapping(path = "/by-username/{username}")
    public PersonTo getByUsername(@PathVariable String username) throws UserNotFoundException {
        return personService.readByUsername(username);
    }

    @GetMapping(path = "/by-uuid/{uuid}")
    public PersonTo getByUuid(@PathVariable String uuid) throws UserNotFoundException {
        return personService.readByUuid(uuid);
    }

    @GetMapping(path = "/self")
    public PersonTo getSelf(Authentication authentication) throws UserNotFoundException {
        return personService.readSelf(authentication);
    }

    @PatchMapping(path = "/self")
    public PersonTo update(Authentication authentication, @RequestBody PersonCreateTo personCreateTo) {
        return personService.update(authentication, personCreateTo);
    }

    @DeleteMapping(path = "/self")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication) {
        personService.deleteSelf(authentication);
    }

    @DeleteMapping(path = "/by-uuid/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUuid(@PathVariable String uuid) throws UserNotFoundException {
        personService.deleteByUuid(uuid);
    }


    @GetMapping
    public Collection<PersonTo> getAll() throws UserNotFoundException {
        return personService.readAll();
    }

    @GetMapping(path = "/followers")
    public Collection<PersonTo> getFollowers(Authentication authentication) throws UserNotFoundException {
        return personService.getFollowers(authentication);
    }

    @GetMapping(path = "/following")
    public Collection<PersonTo> getFollowing(Authentication authentication) throws UserNotFoundException {
        return personService.getFollowing(authentication);
    }

    @GetMapping(path = "/admins")
    public Collection<PersonTo> getAdmins() {
        return personService.getAdmins();
    }

    @PutMapping(path = "/admins/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAdmin(@PathVariable String uuid) throws UserNotFoundException {
        personService.addAdmin(uuid);
    }

    @DeleteMapping(path = "/admins/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdmin(@PathVariable String uuid) throws UserNotFoundException {
        personService.removeAdmin(uuid);
    }

    @PutMapping(path = "/following/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFollowing(Authentication authentication, @PathVariable String uuid) throws UserNotFoundException {
        personService.addFollower(authentication, uuid);
    }

    @DeleteMapping(path = "/following/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFollowing(Authentication authentication, @PathVariable String uuid) throws UserNotFoundException {
        personService.removeFollower(authentication, uuid);
    }

}

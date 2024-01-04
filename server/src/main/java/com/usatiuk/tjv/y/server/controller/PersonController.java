package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.controller.annotations.*;
import com.usatiuk.tjv.y.server.dto.PersonCreateTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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
    @SecurityRequirements(value = {})
    @Operation(summary = "Create a user, user created if there are no admins becomes an admin")
    @PersonToResponse
    @ApiBadRequestResponse
    public PersonTo signup(@RequestBody PersonCreateTo signupRequest) {
        return personService.signup(signupRequest);
    }

    @GetMapping(path = "/by-username/{username}")
    @Operation(summary = "Get a user by username")
    @PersonToResponse
    @ApiNotFoundResponse
    @ApiUnauthorizedResponse
    public PersonTo getByUsername(@PathVariable String username) {
        return personService.readByUsername(username);
    }

    @GetMapping(path = "/by-uuid/{uuid}")
    @Operation(summary = "Get a user by uuid")
    @PersonToResponse
    @ApiNotFoundResponse
    @ApiUnauthorizedResponse
    public PersonTo getByUuid(@PathVariable String uuid) {
        return personService.readByUuid(uuid);
    }

    @GetMapping(path = "/self")
    @Operation(summary = "Get self")
    @PersonToResponse
    @ApiUnauthorizedResponse
    public PersonTo getSelf(Authentication authentication) {
        return personService.readSelf(authentication);
    }

    @PatchMapping(path = "/self")
    @Operation(summary = "Update self")
    @PersonToResponse
    @ApiUnauthorizedResponse
    @ApiBadRequestResponse
    public PersonTo update(Authentication authentication, @RequestBody PersonCreateTo personCreateTo) {
        return personService.update(authentication, personCreateTo);
    }

    @DeleteMapping(path = "/self")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete self")
    @ApiUnauthorizedResponse
    public void delete(Authentication authentication) {
        personService.deleteSelf(authentication);
    }

    @DeleteMapping(path = "/by-uuid/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user by uuid, must be admin")
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    @ApiForbiddenResponse
    public void deleteByUuid(@PathVariable String uuid) {
        personService.deleteByUuid(uuid);
    }


    @GetMapping
    @Operation(summary = "Get all users")
    @ApiUnauthorizedResponse
    @PersonToArrResponse
    public Collection<PersonTo> getAll() {
        return personService.readAll();
    }

    @GetMapping(path = "/followers")
    @Operation(summary = "Get your followers")
    @ApiUnauthorizedResponse
    @PersonToArrResponse
    public Collection<PersonTo> getFollowers(Authentication authentication) {
        return personService.getFollowers(authentication);
    }

    @GetMapping(path = "/following")
    @Operation(summary = "Get who you are following")
    @ApiUnauthorizedResponse
    @PersonToArrResponse
    public Collection<PersonTo> getFollowing(Authentication authentication) {
        return personService.getFollowing(authentication);
    }

    @GetMapping(path = "/admins")
    @Operation(summary = "Get a list of admins")
    @ApiUnauthorizedResponse
    @PersonToArrResponse
    public Collection<PersonTo> getAdmins() {
        return personService.getAdmins();
    }

    @PutMapping(path = "/admins/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add an admin, must be admin self")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @ApiNotFoundResponse
    public void addAdmin(@PathVariable String uuid) {
        personService.addAdmin(uuid);
    }

    @DeleteMapping(path = "/admins/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove an admin, must be admin self")
    @ApiUnauthorizedResponse
    @ApiForbiddenResponse
    @ApiNotFoundResponse
    public void deleteAdmin(@PathVariable String uuid) {
        personService.removeAdmin(uuid);
    }

    @PutMapping(path = "/following/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Follow someone")
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    public void addFollowing(Authentication authentication, @PathVariable String uuid) {
        personService.addFollower(authentication, uuid);
    }

    @DeleteMapping(path = "/following/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Unfollow someone")
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    public void deleteFollowing(Authentication authentication, @PathVariable String uuid) {
        personService.removeFollower(authentication, uuid);
    }

}

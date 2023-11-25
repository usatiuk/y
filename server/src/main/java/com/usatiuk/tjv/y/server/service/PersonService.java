package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;

import java.util.Optional;

public interface PersonService extends CrudService<Person, String> {
    Person signup(Person person) throws UserAlreadyExistsException;

    Optional<Person> login(String username, String password);

    Optional<Person> readByUsername(String username);
}

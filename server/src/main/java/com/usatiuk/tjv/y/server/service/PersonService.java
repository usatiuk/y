package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;

public interface PersonService extends CrudService<Person, String> {
    Person signup(Person person) throws UserAlreadyExistsException;

    Person login(String username, String password) throws UserNotFoundException;

    Person readByUsername(String username) throws UserNotFoundException;
}

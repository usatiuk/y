package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Optional;

public interface PersonService extends CrudService<Person, String> {
    Person signup(Person person) throws UserAlreadyExistsException;

    Optional<Person> login(String username, String password);

    Optional<Person> readByUsername(String username);

    Collection<Person> getFollowers(String uuid) throws UserNotFoundException;
    Collection<Person> getFollowing(String uuid) throws UserNotFoundException;

    void addFollower(String follower, String followee) throws UserNotFoundException;
    void removeFollower(String follower, String followee) throws UserNotFoundException;

    Collection<Person> getAdmins();
    void addAdmin(Authentication caller, String uuid) throws UserNotFoundException;
    void removeAdmin(Authentication caller, String uuid) throws UserNotFoundException;

}

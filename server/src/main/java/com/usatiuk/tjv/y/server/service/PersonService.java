package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.PersonCreateTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.entity.Person;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Optional;

public interface PersonService {
    PersonTo signup(PersonCreateTo person);

    Optional<Person> login(String username, String password);

    PersonTo readByUsername(String username);
    PersonTo readByUuid(String uuid);
    PersonTo readSelf(Authentication authentication);

    PersonTo update(Authentication authentication, PersonCreateTo person);

    void deleteSelf(Authentication authentication);
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.username == #uuid")
    void deleteByUuid(String uuid);

    Collection<PersonTo> readAll();

    Collection<PersonTo> getFollowers(Authentication authentication);
    Collection<PersonTo> getFollowing(Authentication authentication);

    void addFollower(Authentication authentication, String followee);
    void removeFollower(Authentication authentication, String followee);

    Collection<PersonTo> getAdmins();
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    void addAdmin(String uuid);
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    void removeAdmin(String uuid);

}

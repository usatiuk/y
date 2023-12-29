package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, String> {
    Optional<Person> findByUsername(String username);

    @Query(value = "SELECT p from Person p where p.uuid = :usernameOrId or p.username = :usernameOrId")
    Optional<Person> findByUsernameOrId(String usernameOrId);

    boolean existsByUsername(String username);

    Collection<Person> findByAdminIsTrue();
}

package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, String> {
    Optional<Person> findByUsername(String username);

    boolean existsByUsername(String username);
}

package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {
}

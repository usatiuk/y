package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Person;

public interface PersonService extends CrudService<Person, String> {
    Person signup(Person person);
}

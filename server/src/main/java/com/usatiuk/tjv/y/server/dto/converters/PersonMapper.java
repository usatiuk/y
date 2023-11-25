package com.usatiuk.tjv.y.server.dto.converters;

import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.entity.Person;

public class PersonMapper {
    public static PersonTo makeDto(Person person) {
        return new PersonTo(person.getUuid(), person.getUsername(), person.getFullName());
    }
}

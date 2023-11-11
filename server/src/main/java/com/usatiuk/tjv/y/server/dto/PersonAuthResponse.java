package com.usatiuk.tjv.y.server.dto;

import com.usatiuk.tjv.y.server.entity.Person;

public record PersonAuthResponse(String uuid, String username, String fullName, String token) {
    public PersonAuthResponse(Person person, String token) {
        this(person.getId(), person.getUsername(), person.getFullName(), token);
    }
}

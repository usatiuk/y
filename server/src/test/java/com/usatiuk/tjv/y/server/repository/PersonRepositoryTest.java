package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void findByUsernameOrUuidTest() {
        var person = personRepository.save(new Person().setUsername("u1").setFullName("fn").setPassword("pass"));
        
        Assertions.assertEquals(person, personRepository.findByUsernameOrId("u1").get());
        Assertions.assertEquals(person, personRepository.findByUsernameOrId(person.getUuid()).get());
    }
}

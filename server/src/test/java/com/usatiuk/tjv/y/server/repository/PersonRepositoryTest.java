package com.usatiuk.tjv.y.server.repository;

import com.usatiuk.tjv.y.server.entity.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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

    @Test
    void shouldFindAdmins() {
        var nonadmin = personRepository.save(new Person().setUsername("u1").setFullName("fn").setPassword("pass"));
        var admin1 = personRepository.save(new Person().setUsername("u2").setFullName("fn").setPassword("pass").setAdmin(true));
        var admin2 = personRepository.save(new Person().setUsername("u3").setFullName("fn").setPassword("pass").setAdmin(true));

        Assertions.assertIterableEquals(personRepository.findByAdminIsTrue(), List.of(admin1, admin2));
    }
}

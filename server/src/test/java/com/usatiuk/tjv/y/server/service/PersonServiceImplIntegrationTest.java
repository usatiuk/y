package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.PersonCreateTo;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(PER_CLASS)
@Transactional
public class PersonServiceImplIntegrationTest {
    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PersonMapper personMapper;

    protected static final String person1Password = "p1p";
    protected Person person1;
    protected static final String person2Password = "p2p";
    protected Person person2;
    protected static final String person3Password = "p3p";
    protected Person person3;

    @BeforeAll
    void setup() {
        person1 = personRepository.save(
                new Person()
                        .setUsername("person1")
                        .setFullName("Person 1")
                        .setPassword(passwordEncoder.encode(person1Password)));
        person2 = personRepository.save(
                new Person()
                        .setUsername("person2")
                        .setFullName("Person 2")
                        .setPassword(passwordEncoder.encode(person2Password)).setFollowing(new ArrayList<>(List.of(person1))));
        person3 = personRepository.save(
                new Person()
                        .setUsername("person3")
                        .setFullName("Person 3")
                        .setPassword(passwordEncoder.encode(person3Password))
                        .setFollowing(List.of(person2, person1)));
    }

    @AfterAll
    void erase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "person_follows", "chat_person", "post", "message", "chat", "person");
    }

    @Test
    @WithUserDetails(value = "person1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAddFollowing() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        Assertions.assertEquals(0, personService.getFollowing(auth).size());
        personService.addFollower(auth, person2.getUuid());
        Assertions.assertIterableEquals(List.of(personMapper.makeDto(person2)), personService.getFollowing(auth));
    }

    @Test
    @WithUserDetails(value = "person1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAddFollowingThrows() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        Assertions.assertEquals(0, personService.getFollowing(auth).size());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            personService.addFollower(auth, "asdfasdf");
        });
        Assertions.assertEquals(0, personService.getFollowing(auth).size());
    }

    @Test
    @WithUserDetails(value = "person2", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldRemoveFollowing() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        Assertions.assertEquals(1, personService.getFollowing(auth).size());
        personService.removeFollower(auth, person1.getUuid());
        Assertions.assertEquals(0, personService.getFollowing(auth).size());
    }

    @Test
    void firstSignedUpShouldBeAdmin() {
        personService.signup(new PersonCreateTo("newadmin", "full name admin", "adminpass"));
        var created = personService.readByUsername("newadmin");
        Assertions.assertTrue(created.isAdmin());
        Assertions.assertTrue(personService.getAdmins().contains(created));

        personService.signup(new PersonCreateTo("newnotadmin", "full name notadmin", "notadminpass"));
        var created2 = personService.readByUsername("newnotadmin");
        Assertions.assertFalse(created2.isAdmin());
        Assertions.assertFalse(personService.getAdmins().contains(created2));
    }

    @Nested
    @TestInstance(PER_CLASS)
    @Transactional
    class AdminTests {

        @BeforeAll
        void setup() {
            personRepository.save(person1.setAdmin(true));
        }

        @AfterAll
        void teardown() {
            personRepository.save(person1.setAdmin(false));
        }

        @Test
        @WithUserDetails(value = "person1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void shouldAddRemoveAdminIfAdmin() {
            personService.addAdmin(person2.getUuid());
            var updatedP2 = personService.readByUsername(person2.getUsername());
            Assertions.assertTrue(updatedP2.isAdmin());
            Assertions.assertTrue(personService.getAdmins().contains(updatedP2));

            personService.removeAdmin(person2.getUuid());
            updatedP2 = personService.readByUsername(person2.getUsername());
            Assertions.assertFalse(updatedP2.isAdmin());
            Assertions.assertFalse(personService.getAdmins().contains(updatedP2));
        }

        @Test
        @WithUserDetails(value = "person2", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void shouldNotAddRemoveAdminIfNotAdmin() {
            Assertions.assertThrows(AccessDeniedException.class, () ->
                    personService.addAdmin(person2.getUuid())
            );
            var updatedP = personService.readByUsername(person2.getUsername());
            Assertions.assertFalse(updatedP.isAdmin());
            Assertions.assertFalse(personService.getAdmins().contains(updatedP));

            Assertions.assertThrows(AccessDeniedException.class, () ->
                    personService.addAdmin(person3.getUuid())
            );
            var updatedP3 = personService.readByUsername(person3.getUsername());
            Assertions.assertFalse(updatedP3.isAdmin());
            Assertions.assertFalse(personService.getAdmins().contains(updatedP3));

            Assertions.assertThrows(AccessDeniedException.class, () ->
                    personService.removeAdmin(person1.getUuid())
            );
            var updatedP1 = personService.readByUsername(person1.getUsername());
            Assertions.assertTrue(updatedP1.isAdmin());
            Assertions.assertTrue(personService.getAdmins().contains(updatedP1));
        }

    }
}

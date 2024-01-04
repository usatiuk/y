package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.PersonCreateTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import com.usatiuk.tjv.y.server.service.exceptions.ConflictException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@SpringBootTest
public class PersonServiceImplUnitTest {
    @Autowired
    private PersonServiceImpl personService;
    @MockBean
    private PersonRepository personRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private EntityManager entityManager;
    @MockBean
    private PersonMapper personMapper;

    @BeforeEach
    void setup() {
        Mockito.when(personRepository.existsByUsername("userexists")).thenReturn(true);
        Mockito.when(passwordEncoder.encode(any())).thenReturn("encoded");
    }

    @Test
    void shouldNotCreateUserWithConflictingUsername() {
        Assertions.assertThrows(
                ConflictException.class,
                () -> personService.signup(new PersonCreateTo("userexists", "name", "pass"))
        );

        Mockito.verify(personRepository, Mockito.times(1)).existsByUsername("userexists");
        Mockito.verify(personRepository, Mockito.never()).save(any());
        Mockito.verify(passwordEncoder, Mockito.never()).encode(any());
    }

    @Nested
    class AdminTests {
        static class PersonMatcher implements ArgumentMatcher<Person> {
            private final Person left;

            public PersonMatcher(Person l) {
                this.left = l;
            }

            @Override
            public boolean matches(Person right) {
                return left.isAdmin() == right.isAdmin()
                        && Objects.equals(left.getUuid(), right.getUuid())
                        && Objects.equals(left.getUsername(), right.getUsername())
                        && Objects.equals(left.getPassword(), right.getPassword());
            }
        }

        @Test
        void shouldCreateUserAdmin() {
            Mockito.when(personRepository.findByAdminIsTrue()).thenReturn(List.of());
            var personExpected = new Person().setUsername("u1").setFullName("u2").setPassword("encoded").setAdmin(true);
            var repoRet = new Person().setUsername("u1").setFullName("u2").setPassword("encoded").setAdmin(true).setUuid("id1");
            Mockito.when(personRepository.save(argThat(new PersonMatcher(personExpected)))).thenReturn(repoRet);
            var dtoRet = new PersonTo(repoRet.getUuid(), repoRet.getUsername(), repoRet.getFullName(), true);
            Mockito.when(personMapper.makeDto(argThat(new PersonMatcher(repoRet)))).thenReturn(dtoRet);

            var ret = personService.signup(new PersonCreateTo("u1", "u2", "pass"));

            Mockito.verify(personRepository, Mockito.times(1)).existsByUsername("u1");
            Mockito.verify(passwordEncoder, Mockito.times(1)).encode("pass");
            Mockito.verify(personRepository, Mockito.times(1)).save(argThat(new PersonMatcher(personExpected)));
            Mockito.verify(personMapper, Mockito.times(1)).makeDto(argThat(new PersonMatcher(repoRet)));

            Assertions.assertEquals(dtoRet, ret);
        }

        @Test
        void shouldNotCreateUserAdminIfNotFirst() {
            Mockito.when(personRepository.findByAdminIsTrue()).thenReturn(List.of(new Person().setUsername("admin").setAdmin(true)));
            var personExpected = new Person().setUsername("u1").setFullName("u2").setPassword("encoded").setAdmin(false);
            var repoRet = new Person().setUsername("u1").setFullName("u2").setPassword("encoded").setAdmin(false).setUuid("id1");
            Mockito.when(personRepository.save(argThat(new PersonMatcher(personExpected)))).thenReturn(repoRet);
            var dtoRet = new PersonTo(repoRet.getUuid(), repoRet.getUsername(), repoRet.getFullName(), false);
            Mockito.when(personMapper.makeDto(argThat(new PersonMatcher(repoRet)))).thenReturn(dtoRet);

            var ret = personService.signup(new PersonCreateTo("u1", "u2", "pass"));

            Mockito.verify(personRepository, Mockito.times(1)).existsByUsername("u1");
            Mockito.verify(passwordEncoder, Mockito.times(1)).encode("pass");
            Mockito.verify(personRepository, Mockito.times(1)).save(argThat(new PersonMatcher(personExpected)));
            Mockito.verify(personMapper, Mockito.times(1)).makeDto(argThat(new PersonMatcher(repoRet)));

            Assertions.assertEquals(dtoRet, ret);
        }


    }

}

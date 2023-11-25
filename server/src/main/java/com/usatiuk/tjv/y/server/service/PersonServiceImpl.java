package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl extends CrudServiceImpl<Person, String> implements PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonServiceImpl(PersonRepository personRepository,
                             PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected CrudRepository<Person, String> getRepository() {
        return personRepository;
    }

    @Override
    public Person signup(Person person) throws UserAlreadyExistsException {
        if (personRepository.existsByUsername(person.getUsername()))
            throw new UserAlreadyExistsException();

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return create(person);
    }

    @Override
    public Optional<Person> login(String username, String password) {
        var found = personRepository.findByUsername(username);
        if (found.isEmpty() || !passwordEncoder.matches(password, found.get().getPassword()))
            return Optional.empty();

        return found;
    }

    @Override
    public Optional<Person> readByUsername(String username) {
        return personRepository.findByUsername(username);
    }
}

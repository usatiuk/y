package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public Person login(String username, String password) throws UserNotFoundException {
        var found = personRepository.findByUsername(username);
        if (found.isEmpty() || !passwordEncoder.matches(password, found.get().getPassword()))
            throw new UserNotFoundException();

        return found.get();
    }

    @Override
    public Person readByUsername(String username) throws UserNotFoundException {
        var found = personRepository.findByUsername(username);
        if (found.isEmpty())
            throw new UserNotFoundException();

        return found.get();
    }
}

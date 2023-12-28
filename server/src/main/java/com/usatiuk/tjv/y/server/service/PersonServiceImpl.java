package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import com.usatiuk.tjv.y.server.security.UserRoles;
import com.usatiuk.tjv.y.server.service.exceptions.UserAlreadyExistsException;
import com.usatiuk.tjv.y.server.service.exceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@Service
public class PersonServiceImpl extends CrudServiceImpl<Person, String> implements PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public PersonServiceImpl(PersonRepository personRepository,
                             PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
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

        if (personRepository.findByAdminIsTrue().isEmpty()) person.setAdmin(true);

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

    @Override
    public Collection<Person> getFollowers(String uuid) throws UserNotFoundException {
        return personRepository.findById(uuid).orElseThrow(UserNotFoundException::new).getFollowers();
    }

    @Override
    public Collection<Person> getFollowing(String uuid) throws UserNotFoundException {
        return personRepository.findById(uuid).orElseThrow(UserNotFoundException::new).getFollowing();
    }

    @Override
    public void addFollower(String follower, String followee) throws UserNotFoundException {
        var person = personRepository.findById(follower).orElseThrow(UserNotFoundException::new);
        person.getFollowing().add(entityManager.getReference(Person.class, followee));
        personRepository.save(person);
    }

    @Override
    public void removeFollower(String follower, String followee) throws UserNotFoundException {
        var person = personRepository.findById(follower).orElseThrow(UserNotFoundException::new);
        person.getFollowing().remove(entityManager.getReference(Person.class, followee));
        personRepository.save(person);
    }

    @Override
    public void addAdmin(Authentication caller, String uuid) throws UserNotFoundException {
        if (!caller.getAuthorities().contains(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name())))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        var person = personRepository.findById(uuid).orElseThrow(UserNotFoundException::new);
        person.setAdmin(true);
        personRepository.save(person);
    }

    @Override
    public void removeAdmin(Authentication caller, String uuid) throws UserNotFoundException {
        if (!caller.getAuthorities().contains(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name())))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        var person = personRepository.findById(uuid).orElseThrow(UserNotFoundException::new);
        // TODO
        if (personRepository.findByAdminIsTrue().size() == 1) return;

        person.setAdmin(false);
        personRepository.save(person);
    }

    @Override
    public Collection<Person> getAdmins() {
        return personRepository.findByAdminIsTrue();
    }
}

package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.PersonCreateTo;
import com.usatiuk.tjv.y.server.dto.PersonTo;
import com.usatiuk.tjv.y.server.dto.converters.PersonMapper;
import com.usatiuk.tjv.y.server.entity.Chat;
import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import com.usatiuk.tjv.y.server.service.exceptions.ConflictException;
import com.usatiuk.tjv.y.server.service.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository,
                             PasswordEncoder passwordEncoder, EntityManager entityManager, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
        this.personMapper = personMapper;
    }

    @Override
    public PersonTo signup(PersonCreateTo signupRequest) {
        if (personRepository.existsByUsername(signupRequest.username()))
            throw new ConflictException("User already exists with this username");

        Person toCreate = new Person();

        toCreate.setUsername(signupRequest.username())
                .setFullName(signupRequest.fullName());

        toCreate.setPassword(passwordEncoder.encode(signupRequest.password()));

        if (personRepository.findByAdminIsTrue().isEmpty()) toCreate.setAdmin(true);

        return personMapper.makeDto(personRepository.save(toCreate));
    }

    @Override
    public Optional<Person> login(String username, String password) {
        var found = personRepository.findByUsername(username);
        if (found.isEmpty() || !passwordEncoder.matches(password, found.get().getPassword()))
            return Optional.empty();

        return found;
    }

    @Override
    public PersonTo readByUsername(String username) {
        return personMapper.makeDto(personRepository.findByUsername(username).orElseThrow(NotFoundException::new));
    }

    @Override
    public PersonTo readByUuid(String uuid) {
        return personMapper.makeDto(personRepository.findById(uuid).orElseThrow(NotFoundException::new));
    }

    @Override
    public PersonTo readSelf(Authentication authentication) {
        return readByUuid(authentication.getName());
    }

    @Override
    public PersonTo update(Authentication authentication, PersonCreateTo person) {
        var found = personRepository.findById(authentication.getName()).orElseThrow(NotFoundException::new);

        if (!Objects.equals(found.getUsername(), person.username()) && personRepository.existsByUsername(person.username()))
            throw new ConflictException("User already exists with this username");

        found.setUsername(person.username())
                .setFullName(person.fullName());
        if (!person.password().isEmpty()) found.setPassword(passwordEncoder.encode(person.password()));
        personRepository.save(found);
        return personMapper.makeDto(found);
    }

    public void deleteByUuid(String uuid) {
        var person = personRepository.findById(uuid).orElseThrow(NotFoundException::new);
        for (Chat c : person.getChats()) {
            c.getMembers().remove(person);
        }
        for (Person p : person.getFollowers()) {
            p.getFollowing().remove(person);
        }

        personRepository.delete(person);
    }

    @Override
    public void deleteSelf(Authentication authentication) {
        deleteByUuid(authentication.getName());
    }


    @Override
    public Collection<PersonTo> readAll() {
        return StreamSupport.stream(personRepository.findAll().spliterator(), false).map(personMapper::makeDto).toList();
    }

    @Override
    public Collection<PersonTo> getFollowers(Authentication authentication) {
        return personRepository.findById(authentication.getName()).orElseThrow(NotFoundException::new)
                .getFollowers().stream().map(personMapper::makeDto).toList();
    }

    @Override
    public Collection<PersonTo> getFollowing(Authentication authentication) {
        return personRepository.findById(authentication.getName()).orElseThrow(NotFoundException::new)
                .getFollowing().stream().map(personMapper::makeDto).toList();
    }

    @Override
    public void addFollower(Authentication authentication, String followee) {
        var person = personRepository.findById(authentication.getName()).orElseThrow(NotFoundException::new);
        person.getFollowing().add(entityManager.getReference(Person.class, followee));
        personRepository.save(person);
    }

    @Override
    public void removeFollower(Authentication authentication, String followee) {
        var person = personRepository.findById(authentication.getName()).orElseThrow(NotFoundException::new);
        person.getFollowing().remove(entityManager.getReference(Person.class, followee));
        personRepository.save(person);
    }

    @Override
    public void addAdmin(String uuid) {
        var person = personRepository.findById(uuid).orElseThrow(NotFoundException::new);
        person.setAdmin(true);
        personRepository.save(person);
    }

    @Override
    public void removeAdmin(String uuid) {
        var person = personRepository.findById(uuid).orElseThrow(NotFoundException::new);
        person.setAdmin(false);
        personRepository.save(person);
    }

    @Override
    public Collection<PersonTo> getAdmins() {
        return personRepository.findByAdminIsTrue().stream().map(personMapper::makeDto).toList();
    }
}

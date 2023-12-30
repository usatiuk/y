package com.usatiuk.tjv.y.server.security;

import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.repository.PersonRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final PersonRepository personRepository;

    public UserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrUuid) {
        Person person = personRepository.findByUsernameOrId(usernameOrUuid).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ArrayList<SimpleGrantedAuthority> roles =
                new ArrayList<>(List.of(new SimpleGrantedAuthority(UserRoles.ROLE_USER.name())));
        if (person.isAdmin()) roles.add(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name()));
        return new User(person.getUuid(), person.getPassword(), roles);
    }

}
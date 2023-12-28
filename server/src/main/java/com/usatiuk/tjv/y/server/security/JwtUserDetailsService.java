package com.usatiuk.tjv.y.server.security;

import com.usatiuk.tjv.y.server.entity.Person;
import com.usatiuk.tjv.y.server.service.PersonService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final PersonService personService;

    public JwtUserDetailsService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String uuid) {
        Optional<Person> person = personService.readById(uuid);
        if (!person.isPresent()) throw new UsernameNotFoundException("User with UUID " + uuid + " not found");
        ArrayList<SimpleGrantedAuthority> roles =
                new ArrayList<>(List.of(new SimpleGrantedAuthority(UserRoles.ROLE_USER.name())));
        if (person.get().isAdmin()) roles.add(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name()));
        return new JwtUser(uuid, person.get().getPassword(), roles);
    }

}
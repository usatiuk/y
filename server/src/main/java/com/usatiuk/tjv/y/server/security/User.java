package com.usatiuk.tjv.y.server.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class User extends org.springframework.security.core.userdetails.User {

    public User(String uuid, String hash,
                Collection<? extends GrantedAuthority> authorities) {
        super(uuid, hash, authorities);
    }
}
package com.usatiuk.tjv.y.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUser extends User {

    public JwtUser(String uuid, String hash,
                   Collection<? extends GrantedAuthority> authorities) {
        super(uuid, hash, authorities);
    }
}
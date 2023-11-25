package com.usatiuk.tjv.y.server.service;

import java.util.Optional;

public interface TokenService {
    String generateToken(String personUuid);

    Optional<String> parseToken(String token);
}

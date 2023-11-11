package com.usatiuk.tjv.y.server.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class PersonTokenServiceImpl implements PersonTokenService {
    private static final Duration JWT_EXPIRY = Duration.ofMinutes(20);

    private final SecretKey key;

    public PersonTokenServiceImpl(@Value("${jwt.secret}") String secret) {
        // FIXME:
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(Encoders.BASE64.encode(secret.getBytes())));
    }

    @Override
    public String generateToken(String personUuid) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(personUuid)
                .expiration(Date.from(now.plus(JWT_EXPIRY)))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public Optional<String> parseToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            if (claims.getExpiration().before(new Date())) return Optional.empty();
            return Optional.of(claims.getSubject());
        } catch (JwtException ex) {
            return Optional.empty();
        }
    }
}

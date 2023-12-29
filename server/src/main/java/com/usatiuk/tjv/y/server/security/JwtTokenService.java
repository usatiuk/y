package com.usatiuk.tjv.y.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenService {
    private final SecretKey key;
    private final Duration jwtExpiry;

    public JwtTokenService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiryMinutes}") Long expiry) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpiry = Duration.ofMinutes(expiry);
    }

    public String generateToken(String personUuid) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(personUuid)
                .expiration(Date.from(now.plus(jwtExpiry)))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public Optional<String> getPersonUuidFromToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            if (claims.getExpiration().before(new Date())) return Optional.empty();
            return Optional.of(claims.getSubject());
        } catch (JwtException ex) {
            return Optional.empty();
        }
    }

}

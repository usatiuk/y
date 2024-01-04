package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.TokenRequestTo;
import com.usatiuk.tjv.y.server.dto.TokenResponseTo;
import com.usatiuk.tjv.y.server.security.JwtTokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LoginTokenServiceImpl implements LoginTokenService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public LoginTokenServiceImpl(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public TokenResponseTo login(TokenRequestTo tokenRequestTo) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(tokenRequestTo.username(), tokenRequestTo.password()));
        String uuid = authentication.getName();
        String token = jwtTokenService.generateToken(uuid);
        return new TokenResponseTo(token);
    }
}

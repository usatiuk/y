package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.dto.TokenRequestTo;
import com.usatiuk.tjv.y.server.dto.TokenResponseTo;

public interface LoginTokenService {
    TokenResponseTo login(TokenRequestTo tokenRequestTo);
}

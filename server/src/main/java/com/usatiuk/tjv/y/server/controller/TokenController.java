package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.controller.annotations.ApiUnauthorizedResponse;
import com.usatiuk.tjv.y.server.dto.TokenRequestTo;
import com.usatiuk.tjv.y.server.dto.TokenResponseTo;
import com.usatiuk.tjv.y.server.service.LoginTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {
    private final LoginTokenService loginTokenService;

    public TokenController(LoginTokenService loginTokenService) {
        this.loginTokenService = loginTokenService;
    }

    @PostMapping
    @SecurityRequirements(value = {})
    @Operation(summary = "Get a token (login)")
    @ApiUnauthorizedResponse
    @ApiResponse(
            responseCode = "200",
            description = "Returns a token",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponseTo.class)
            ))
    public TokenResponseTo request(@RequestBody TokenRequestTo tokenRequestTo) {
        return loginTokenService.login(tokenRequestTo);
    }

}

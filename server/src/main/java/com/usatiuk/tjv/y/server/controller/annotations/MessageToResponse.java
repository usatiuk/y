package com.usatiuk.tjv.y.server.controller.annotations;

import com.usatiuk.tjv.y.server.dto.MessageTo;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "200",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MessageTo.class)
        ))
public @interface MessageToResponse {
}

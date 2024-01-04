package com.usatiuk.tjv.y.server.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestTo(@NotBlank String username, @NotBlank String password) {
}

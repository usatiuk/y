package com.usatiuk.tjv.y.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PersonCreateTo(@NotBlank @Size(max = 100) String username, @NotBlank @Size(max = 100) String fullName,
                             @NotBlank String password) {
}

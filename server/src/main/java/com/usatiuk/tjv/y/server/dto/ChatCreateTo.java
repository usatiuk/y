package com.usatiuk.tjv.y.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatCreateTo(@NotBlank String name,
                           @NotBlank @Size(min = 2) String[] memberUuids) {
}

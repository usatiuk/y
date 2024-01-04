package com.usatiuk.tjv.y.server.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreateTo(@NotBlank String text) {
}

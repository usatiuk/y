package com.usatiuk.tjv.y.server.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageCreateTo(@NotBlank String contents) {
}

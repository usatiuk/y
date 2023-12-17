package com.usatiuk.tjv.y.server.dto;

public record PostTo(Long id, String authorUuid, String authorUsername, String text, Long createdAt) {
}

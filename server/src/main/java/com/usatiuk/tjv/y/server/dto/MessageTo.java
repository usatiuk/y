package com.usatiuk.tjv.y.server.dto;

public record MessageTo(Long id, Long chatId, String authorUuid, String contents) {
}

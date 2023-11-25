package com.usatiuk.tjv.y.server.dto;

import com.usatiuk.tjv.y.server.entity.Post;

public record PostTo(Long id, String authorUuid, String text) {
}

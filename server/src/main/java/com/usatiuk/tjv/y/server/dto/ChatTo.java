package com.usatiuk.tjv.y.server.dto;

public record ChatTo(Long id, String name, String creatorUuid, PersonTo[] memberUuids, MessageTo[] messages) {

}

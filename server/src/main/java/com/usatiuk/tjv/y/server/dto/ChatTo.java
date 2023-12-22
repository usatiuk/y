package com.usatiuk.tjv.y.server.dto;

import java.util.Collection;

public record ChatTo(Long id, String name, String creatorUuid, Collection<PersonTo> members,
                     Collection<MessageTo> messages) {

}

package com.usatiuk.tjv.y.server.dto;

import com.usatiuk.tjv.y.server.entity.Post;

public record PostTo(Long id, String authorUuid, String text) {
    public PostTo(Post post) {
        this(post.getId(), post.getAuthor().getUuid(), post.getText());
    }
}

package com.usatiuk.tjv.y.server.dto.converters;

import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.entity.Post;

public class PostMapper {
    public static PostTo makeDto(Post post) {
        return new PostTo(post.getId(), post.getAuthor().getUuid(), post.getText(), post.getCreatedAt().getEpochSecond());
    }
}

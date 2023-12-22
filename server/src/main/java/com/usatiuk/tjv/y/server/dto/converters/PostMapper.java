package com.usatiuk.tjv.y.server.dto.converters;

import com.usatiuk.tjv.y.server.dto.PostTo;
import com.usatiuk.tjv.y.server.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostTo makeDto(Post post) {
        return new PostTo(post.getId(), post.getAuthor().getUuid(), post.getAuthor().getUsername(), post.getText(), post.getCreatedAt().getEpochSecond());
    }
}

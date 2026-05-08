package com.example.secureboardexample.domain.post.dto;

import com.example.secureboardexample.domain.post.entity.Post;
import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long userId,
        String authorNickname,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}

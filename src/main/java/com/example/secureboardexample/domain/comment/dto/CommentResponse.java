package com.example.secureboardexample.domain.comment.dto;

import com.example.secureboardexample.domain.comment.entity.Comment;
import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        Long userId,
        String authorNickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}

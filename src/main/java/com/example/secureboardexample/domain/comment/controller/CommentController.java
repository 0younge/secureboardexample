package com.example.secureboardexample.domain.comment.controller;

import com.example.secureboardexample.domain.comment.dto.CommentResponse;
import com.example.secureboardexample.domain.comment.dto.CreateCommentRequest;
import com.example.secureboardexample.domain.comment.dto.UpdateCommentRequest;
import com.example.secureboardexample.domain.comment.service.CommentService;
import com.example.secureboardexample.global.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        CommentResponse response = commentService.createComment(postId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "댓글 작성 성공", response));
    }

    @GetMapping("/api/posts/{postId}/comments")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ApiResponse.of(200, "댓글 목록 조회 성공", commentService.getComments(postId));
    }

    @PatchMapping("/api/comments/{commentId}")
    public ApiResponse<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        return ApiResponse.of(200, "댓글 수정 성공", commentService.updateComment(commentId, request));
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.message(200, "댓글 삭제 성공");
    }
}

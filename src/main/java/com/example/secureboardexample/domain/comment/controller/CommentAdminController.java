package com.example.secureboardexample.domain.comment.controller;

import com.example.secureboardexample.domain.comment.service.CommentService;
import com.example.secureboardexample.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
        return ApiResponse.message(200, "관리자 댓글 삭제 성공");
    }
}

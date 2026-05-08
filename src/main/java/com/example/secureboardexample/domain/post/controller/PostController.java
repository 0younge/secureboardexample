package com.example.secureboardexample.domain.post.controller;

import com.example.secureboardexample.domain.post.dto.CreatePostRequest;
import com.example.secureboardexample.domain.post.dto.PostResponse;
import com.example.secureboardexample.domain.post.dto.UpdatePostRequest;
import com.example.secureboardexample.domain.post.service.PostService;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@Valid @RequestBody CreatePostRequest request) {
        PostResponse response = postService.createPost(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "게시글 작성 성공", response));
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {
        return ApiResponse.of(200, "게시글 목록 조회 성공", postService.getPosts());
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        return ApiResponse.of(200, "게시글 조회 성공", postService.getPost(postId));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        return ApiResponse.of(200, "게시글 수정 성공", postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.message(200, "게시글 삭제 성공");
    }
}

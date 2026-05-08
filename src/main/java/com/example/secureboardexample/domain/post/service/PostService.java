package com.example.secureboardexample.domain.post.service;

import com.example.secureboardexample.domain.comment.repository.CommentRepository;
import com.example.secureboardexample.domain.post.dto.CreatePostRequest;
import com.example.secureboardexample.domain.post.dto.PostResponse;
import com.example.secureboardexample.domain.post.dto.UpdatePostRequest;
import com.example.secureboardexample.domain.post.entity.Post;
import com.example.secureboardexample.domain.post.repository.PostRepository;
import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.domain.user.service.UserService;
import com.example.secureboardexample.global.exception.CustomException;
import com.example.secureboardexample.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        User user = userService.getUserEntity(request.userId());
        Post post = new Post(user, request.title(), request.content());

        return PostResponse.from(postRepository.save(post));
    }

    public List<PostResponse> getPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse getPost(Long postId) {
        return PostResponse.from(getPostEntity(postId));
    }

    public Post getPostEntity(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request) {
        Post post = getPostEntity(postId);
        post.update(request.title(), request.content());

        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = getPostEntity(postId);
        commentRepository.deleteAllByPostId(postId);
        postRepository.delete(post);
    }
}

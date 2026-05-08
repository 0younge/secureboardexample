package com.example.secureboardexample.domain.comment.service;

import com.example.secureboardexample.domain.comment.dto.CommentResponse;
import com.example.secureboardexample.domain.comment.dto.CreateCommentRequest;
import com.example.secureboardexample.domain.comment.dto.UpdateCommentRequest;
import com.example.secureboardexample.domain.comment.entity.Comment;
import com.example.secureboardexample.domain.comment.repository.CommentRepository;
import com.example.secureboardexample.domain.post.entity.Post;
import com.example.secureboardexample.domain.post.service.PostService;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public CommentResponse createComment(Long postId, CreateCommentRequest request) {
        Post post = postService.getPostEntity(postId);
        User user = userService.getUserEntity(request.userId());
        Comment comment = new Comment(post, user, request.content());

        return CommentResponse.from(commentRepository.save(comment));
    }

    public List<CommentResponse> getComments(Long postId) {
        postService.getPostEntity(postId);

        return commentRepository.findAllByPostId(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    public Comment getCommentEntity(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = getCommentEntity(commentId);
        comment.update(request.content());

        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = getCommentEntity(commentId);
        commentRepository.delete(comment);
    }
}

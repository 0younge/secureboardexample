package com.example.secureboardexample.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.secureboardexample.domain.comment.dto.CommentResponse;
import com.example.secureboardexample.domain.comment.dto.CreateCommentRequest;
import com.example.secureboardexample.domain.comment.dto.UpdateCommentRequest;
import com.example.secureboardexample.domain.post.dto.CreatePostRequest;
import com.example.secureboardexample.domain.post.dto.PostResponse;
import com.example.secureboardexample.domain.post.service.PostService;
import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.domain.user.entity.UserRole;
import com.example.secureboardexample.domain.user.repository.UserRepository;
import com.example.secureboardexample.global.exception.CustomException;
import com.example.secureboardexample.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createAndUpdateComment() {
        User user = saveUser("comment-user@test.com");
        PostResponse post = postService.createPost(user.getId(), new CreatePostRequest("게시글", "내용"));

        CommentResponse created = commentService.createComment(user.getId(), post.id(), new CreateCommentRequest("댓글"));
        CommentResponse updated = commentService.updateComment(user.getId(), created.id(), new UpdateCommentRequest("수정 댓글"));

        assertThat(created.postId()).isEqualTo(post.id());
        assertThat(updated.content()).isEqualTo("수정 댓글");
    }

    @Test
    void onlyAuthorCanUpdateComment() {
        User author = saveUser("comment-author@test.com");
        User other = saveUser("comment-other@test.com");
        PostResponse post = postService.createPost(author.getId(), new CreatePostRequest("게시글", "내용"));
        CommentResponse comment = commentService.createComment(author.getId(), post.id(), new CreateCommentRequest("댓글"));

        assertThatThrownBy(() -> commentService.updateComment(other.getId(), comment.id(), new UpdateCommentRequest("수정")))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN);
    }

    @Test
    void getCommentsRequiresExistingPost() {
        assertThatThrownBy(() -> commentService.getComments(0L))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

    @Test
    void updateUnknownCommentThrowsException() {
        assertThatThrownBy(() -> commentService.updateComment(1L, 0L, new UpdateCommentRequest("수정")))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
    }

    private User saveUser(String email) {
        return userRepository.save(User.builder()
                .email(email)
                .password("password")
                .nickname("댓글작성자")
                .role(UserRole.USER)
                .build());
    }
}

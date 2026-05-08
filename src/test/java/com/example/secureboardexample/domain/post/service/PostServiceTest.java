package com.example.secureboardexample.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.secureboardexample.domain.comment.dto.CreateCommentRequest;
import com.example.secureboardexample.domain.comment.repository.CommentRepository;
import com.example.secureboardexample.domain.comment.service.CommentService;
import com.example.secureboardexample.domain.post.dto.CreatePostRequest;
import com.example.secureboardexample.domain.post.dto.PostResponse;
import com.example.secureboardexample.domain.post.dto.UpdatePostRequest;
import com.example.secureboardexample.domain.post.repository.PostRepository;
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
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void createAndUpdatePost() {
        User user = saveUser("post-user@test.com");

        PostResponse created = postService.createPost(user.getId(), new CreatePostRequest("첫 게시글", "내용입니다"));
        PostResponse updated = postService.updatePost(user.getId(), created.id(), new UpdatePostRequest("수정 제목", "수정 내용"));

        assertThat(created.userId()).isEqualTo(user.getId());
        assertThat(updated.title()).isEqualTo("수정 제목");
        assertThat(updated.content()).isEqualTo("수정 내용");
    }

    @Test
    void onlyAuthorCanUpdatePost() {
        User author = saveUser("post-author@test.com");
        User other = saveUser("post-other@test.com");
        PostResponse post = postService.createPost(author.getId(), new CreatePostRequest("작성자 글", "내용"));

        assertThatThrownBy(() -> postService.updatePost(other.getId(), post.id(), new UpdatePostRequest("수정", "수정")))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN);
    }

    @Test
    void deletePostAlsoDeletesComments() {
        User user = saveUser("post-delete-user@test.com");
        PostResponse post = postService.createPost(user.getId(), new CreatePostRequest("삭제할 글", "내용"));
        commentService.createComment(user.getId(), post.id(), new CreateCommentRequest("댓글"));

        postService.deletePost(user.getId(), user.getRole(), post.id());

        assertThat(postRepository.findById(post.id())).isEmpty();
        assertThat(commentRepository.findAllByPostId(post.id())).isEmpty();
    }

    @Test
    void getUnknownPostThrowsException() {
        assertThatThrownBy(() -> postService.getPost(0L))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

    private User saveUser(String email) {
        return userRepository.save(User.builder()
                .email(email)
                .password("password")
                .nickname("작성자")
                .role(UserRole.USER)
                .build());
    }
}

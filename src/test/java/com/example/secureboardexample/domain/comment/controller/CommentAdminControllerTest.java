package com.example.secureboardexample.domain.comment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.secureboardexample.domain.adminlog.repository.AdminLogRepository;
import com.example.secureboardexample.domain.comment.dto.CommentResponse;
import com.example.secureboardexample.domain.comment.dto.CreateCommentRequest;
import com.example.secureboardexample.domain.comment.repository.CommentRepository;
import com.example.secureboardexample.domain.comment.service.CommentService;
import com.example.secureboardexample.domain.post.dto.CreatePostRequest;
import com.example.secureboardexample.domain.post.dto.PostResponse;
import com.example.secureboardexample.domain.post.service.PostService;
import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.domain.user.entity.UserRole;
import com.example.secureboardexample.domain.user.repository.UserRepository;
import com.example.secureboardexample.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AdminLogRepository adminLogRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void userCannotForceDeleteComment() throws Exception {
        User user = saveUser("comment-admin-user@test.com", UserRole.USER);
        CommentResponse comment = saveComment(user);
        String token = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());

        mockMvc.perform(delete("/api/admin/comments/{commentId}", comment.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        assertThat(commentRepository.findById(comment.id())).isPresent();
    }

    @Test
    void adminCanForceDeleteComment() throws Exception {
        User user = saveUser("comment-admin-target@test.com", UserRole.USER);
        User admin = saveUser("comment-admin@test.com", UserRole.ADMIN);
        CommentResponse comment = saveComment(user);
        String token = jwtTokenProvider.createAccessToken(admin.getId(), admin.getRole());

        mockMvc.perform(delete("/api/admin/comments/{commentId}", comment.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.id())).isEmpty();
        assertThat(adminLogRepository.findAll()).anySatisfy(adminLog -> {
            assertThat(adminLog.getAdminUser().getId()).isEqualTo(admin.getId());
            assertThat(adminLog.getRequestUrl()).isEqualTo("/api/admin/comments/" + comment.id());
            assertThat(adminLog.getHttpMethod()).isEqualTo("DELETE");
        });
    }

    private CommentResponse saveComment(User user) {
        PostResponse post = postService.createPost(user.getId(), new CreatePostRequest("관리 댓글 글", "내용"));
        return commentService.createComment(user.getId(), post.id(), new CreateCommentRequest("관리 댓글"));
    }

    private User saveUser(String email, UserRole role) {
        return userRepository.save(User.builder()
                .email(email)
                .password("password")
                .nickname("댓글관리")
                .role(role)
                .build());
    }
}

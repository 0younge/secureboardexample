package com.example.secureboardexample.domain.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.domain.user.entity.UserRole;
import com.example.secureboardexample.domain.user.repository.UserRepository;
import com.example.secureboardexample.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void userCannotChangeRole() throws Exception {
        User user = saveUser("admin-user@test.com", UserRole.USER);
        String token = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());

        mockMvc.perform(patch("/api/admin/users/{userId}/role", user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "ADMIN"
                                }
                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"));
    }

    @Test
    void adminCanChangeRole() throws Exception {
        User admin = saveUser("admin@test.com", UserRole.ADMIN);
        User user = saveUser("role-target@test.com", UserRole.USER);
        String token = jwtTokenProvider.createAccessToken(admin.getId(), admin.getRole());

        mockMvc.perform(patch("/api/admin/users/{userId}/role", user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "ADMIN"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));

        assertThat(userRepository.findById(user.getId()).orElseThrow().getRole()).isEqualTo(UserRole.ADMIN);
    }

    private User saveUser(String email, UserRole role) {
        return userRepository.save(User.builder()
                .email(email)
                .password("password")
                .nickname("관리테스트")
                .role(role)
                .build());
    }
}

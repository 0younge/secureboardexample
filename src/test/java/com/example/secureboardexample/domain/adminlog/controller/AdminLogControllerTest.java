package com.example.secureboardexample.domain.adminlog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.secureboardexample.domain.adminlog.entity.AdminLog;
import com.example.secureboardexample.domain.adminlog.repository.AdminLogRepository;
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
class AdminLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminLogRepository adminLogRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void adminApiRequestIsSavedAndReadable() throws Exception {
        User admin = saveUser("log-admin@test.com", UserRole.ADMIN);
        User target = saveUser("log-target@test.com", UserRole.USER);
        String token = jwtTokenProvider.createAccessToken(admin.getId(), admin.getRole());

        mockMvc.perform(patch("/api/admin/users/{userId}/role", target.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "ADMIN"
                                }
                                """))
                .andExpect(status().isOk());

        AdminLog savedLog = adminLogRepository.findAll().get(0);

        assertThat(savedLog.getAdminUser().getId()).isEqualTo(admin.getId());
        assertThat(savedLog.getRequestUrl()).isEqualTo("/api/admin/users/" + target.getId() + "/role");
        assertThat(savedLog.getHttpMethod()).isEqualTo("PATCH");
        assertThat(savedLog.getRequestBody()).contains("ADMIN");
        assertThat(savedLog.getResponseBody()).contains("ADMIN");

        mockMvc.perform(get("/api/admin/logs")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].adminUserId").value(admin.getId()))
                .andExpect(jsonPath("$[0].requestUrl").value("/api/admin/users/" + target.getId() + "/role"))
                .andExpect(jsonPath("$[0].httpMethod").value("PATCH"));
    }

    private User saveUser(String email, UserRole role) {
        return userRepository.save(User.builder()
                .email(email)
                .password("password")
                .nickname("로그관리")
                .role(role)
                .build());
    }
}

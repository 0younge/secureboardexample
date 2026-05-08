package com.example.secureboardexample.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.secureboardexample.domain.auth.dto.LoginRequest;
import com.example.secureboardexample.domain.auth.dto.LoginResponse;
import com.example.secureboardexample.domain.auth.dto.SignupRequest;
import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.domain.user.repository.UserRepository;
import com.example.secureboardexample.global.exception.CustomException;
import com.example.secureboardexample.global.exception.ErrorCode;
import com.example.secureboardexample.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void signupEncodesPassword() {
        authService.signup(new SignupRequest("signup@test.com", "1234", "회원"));

        User user = userRepository.findByEmail("signup@test.com").orElseThrow();

        assertThat(user.getPassword()).isNotEqualTo("1234");
        assertThat(passwordEncoder.matches("1234", user.getPassword())).isTrue();
    }

    @Test
    void duplicateEmailSignupFails() {
        SignupRequest request = new SignupRequest("duplicate@test.com", "1234", "회원");
        authService.signup(request);

        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    void loginReturnsValidJwt() {
        authService.signup(new SignupRequest("login@test.com", "1234", "로그인회원"));

        LoginResponse response = authService.login(new LoginRequest("login@test.com", "1234"));
        Long userId = userRepository.findByEmail("login@test.com").orElseThrow().getId();

        assertThat(response.accessToken()).isNotBlank();
        assertThat(jwtTokenProvider.isValidToken(response.accessToken())).isTrue();
        assertThat(jwtTokenProvider.getUserId(response.accessToken())).isEqualTo(userId);
    }

    @Test
    void loginWithWrongPasswordFails() {
        authService.signup(new SignupRequest("wrong-password@test.com", "1234", "회원"));

        assertThatThrownBy(() -> authService.login(new LoginRequest("wrong-password@test.com", "wrong")))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_CREDENTIALS);
    }
}

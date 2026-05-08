package com.example.secureboardexample.domain.user.controller;

import com.example.secureboardexample.domain.user.dto.UserResponse;
import com.example.secureboardexample.domain.user.service.UserService;
import com.example.secureboardexample.global.common.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.of(200, "사용자 목록 조회 성공", userService.getUsers());
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
        return ApiResponse.of(200, "사용자 조회 성공", userService.getUser(userId));
    }
}

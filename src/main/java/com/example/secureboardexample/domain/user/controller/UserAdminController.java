package com.example.secureboardexample.domain.user.controller;

import com.example.secureboardexample.domain.user.dto.ChangeUserRoleRequest;
import com.example.secureboardexample.domain.user.dto.ChangeUserRoleResponse;
import com.example.secureboardexample.domain.user.service.UserService;
import com.example.secureboardexample.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @PatchMapping("/{userId}/role")
    public ApiResponse<ChangeUserRoleResponse> changeUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeUserRoleRequest request
    ) {
        return ApiResponse.of(200, "사용자 권한 변경 성공", userService.changeUserRole(userId, request.role()));
    }
}

package com.example.secureboardexample.domain.user.dto;

import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.domain.user.entity.UserRole;

public record ChangeUserRoleResponse(
        Long id,
        UserRole role
) {

    public static ChangeUserRoleResponse from(User user) {
        return new ChangeUserRoleResponse(user.getId(), user.getRole());
    }
}

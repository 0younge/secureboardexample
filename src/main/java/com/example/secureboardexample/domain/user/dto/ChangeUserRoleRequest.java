package com.example.secureboardexample.domain.user.dto;

import com.example.secureboardexample.domain.user.entity.UserRole;
import jakarta.validation.constraints.NotNull;

public record ChangeUserRoleRequest(
        @NotNull(message = "변경할 권한은 필수입니다.")
        UserRole role
) {
}

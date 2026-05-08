package com.example.secureboardexample.domain.adminlog.dto;

import com.example.secureboardexample.domain.adminlog.entity.AdminLog;
import java.time.LocalDateTime;

public record AdminLogResponse(
        Long id,
        Long adminUserId,
        String requestUrl,
        String httpMethod,
        String requestBody,
        String responseBody,
        LocalDateTime createdAt
) {

    public static AdminLogResponse from(AdminLog adminLog) {
        return new AdminLogResponse(
                adminLog.getId(),
                adminLog.getAdminUser().getId(),
                adminLog.getRequestUrl(),
                adminLog.getHttpMethod(),
                adminLog.getRequestBody(),
                adminLog.getResponseBody(),
                adminLog.getCreatedAt()
        );
    }
}

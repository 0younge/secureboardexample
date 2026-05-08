package com.example.secureboardexample.global.security;

import com.example.secureboardexample.global.exception.ErrorCode;
import com.example.secureboardexample.global.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.FORBIDDEN);

        response.setStatus(ErrorCode.FORBIDDEN.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
                {"status":%d,"code":"%s","message":"%s","timestamp":"%s"}
                """.formatted(
                errorResponse.status(),
                errorResponse.code(),
                errorResponse.message(),
                errorResponse.timestamp()
        ));
    }
}

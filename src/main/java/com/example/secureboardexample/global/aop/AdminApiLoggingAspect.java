package com.example.secureboardexample.global.aop;

import com.example.secureboardexample.domain.adminlog.service.AdminLogService;
import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.global.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminApiLoggingAspect {

    private final AdminLogService adminLogService;

    @Around("""
            execution(* com.example.secureboardexample.domain.user.controller.UserAdminController.changeUserRole(..)) ||
            execution(* com.example.secureboardexample.domain.comment.controller.CommentAdminController.deleteComment(..))
            """)
    public Object saveAdminApiLog(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getCurrentRequest();
        User adminUser = getCurrentAdminUser();
        String requestBody = extractRequestBody(joinPoint.getArgs());
        Object result = null;
        Throwable throwable = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable exception) {
            throwable = exception;
            throw exception;
        } finally {
            String responseBody = throwable == null ? String.valueOf(result) : throwable.getClass().getSimpleName() + ": " + throwable.getMessage();

            log.info("Admin API response. method={}, uri={}, requestBody={}, responseBody={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    requestBody,
                    responseBody);

            adminLogService.saveLog(
                    adminUser,
                    request.getRequestURI(),
                    request.getMethod(),
                    requestBody,
                    responseBody
            );
        }
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }

    private User getCurrentAdminUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }

        throw new IllegalStateException("관리자 인증 정보를 찾을 수 없습니다.");
    }

    private String extractRequestBody(Object[] args) {
        return Arrays.stream(args)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}

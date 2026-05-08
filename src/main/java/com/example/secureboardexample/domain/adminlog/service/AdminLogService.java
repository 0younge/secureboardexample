package com.example.secureboardexample.domain.adminlog.service;

import com.example.secureboardexample.domain.adminlog.dto.AdminLogResponse;
import com.example.secureboardexample.domain.adminlog.entity.AdminLog;
import com.example.secureboardexample.domain.adminlog.repository.AdminLogRepository;
import com.example.secureboardexample.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminLogService {

    private final AdminLogRepository adminLogRepository;

    @Transactional
    public void saveLog(User adminUser, String requestUrl, String httpMethod, String requestBody, String responseBody) {
        AdminLog adminLog = AdminLog.builder()
                .adminUser(adminUser)
                .requestUrl(requestUrl)
                .httpMethod(httpMethod)
                .requestBody(requestBody)
                .responseBody(responseBody)
                .build();

        adminLogRepository.save(adminLog);
    }

    public List<AdminLogResponse> getLogs() {
        return adminLogRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(AdminLogResponse::from)
                .toList();
    }
}

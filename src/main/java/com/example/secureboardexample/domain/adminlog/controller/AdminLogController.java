package com.example.secureboardexample.domain.adminlog.controller;

import com.example.secureboardexample.domain.adminlog.dto.AdminLogResponse;
import com.example.secureboardexample.domain.adminlog.service.AdminLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/logs")
@RequiredArgsConstructor
public class AdminLogController {

    private final AdminLogService adminLogService;

    @GetMapping
    public List<AdminLogResponse> getLogs() {
        return adminLogService.getLogs();
    }
}

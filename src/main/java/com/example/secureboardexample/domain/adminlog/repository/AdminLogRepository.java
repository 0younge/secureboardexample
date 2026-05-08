package com.example.secureboardexample.domain.adminlog.repository;

import com.example.secureboardexample.domain.adminlog.entity.AdminLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {

    List<AdminLog> findAllByOrderByCreatedAtDesc();
}

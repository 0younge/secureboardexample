package com.example.secureboardexample.domain.adminlog.entity;

import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "admin_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id", nullable = false)
    private User adminUser;

    @Column(nullable = false)
    private String requestUrl;

    @Column(nullable = false, length = 20)
    private String httpMethod;

    @Lob
    @Column(nullable = false)
    private String requestBody;

    @Lob
    @Column(nullable = false)
    private String responseBody;

    @Builder
    public AdminLog(User adminUser, String requestUrl, String httpMethod, String requestBody, String responseBody) {
        this.adminUser = adminUser;
        this.requestUrl = requestUrl;
        this.httpMethod = httpMethod;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
    }
}

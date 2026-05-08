package com.example.secureboardexample.domain.user.service;

import com.example.secureboardexample.domain.user.dto.UserResponse;
import com.example.secureboardexample.domain.user.entity.User;
import com.example.secureboardexample.domain.user.repository.UserRepository;
import com.example.secureboardexample.global.exception.CustomException;
import com.example.secureboardexample.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponse getUser(Long userId) {
        return UserResponse.from(getUserEntity(userId));
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }
}

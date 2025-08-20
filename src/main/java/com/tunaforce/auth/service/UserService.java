package com.tunaforce.auth.service;

import com.tunaforce.auth.entity.User;
import com.tunaforce.auth.exception.UserNotFoundException;
import com.tunaforce.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 게이트웨이가 전달한 헤더 X-User-Id로 사용자 식별해 소프트 삭제 수행
     * - deletedAt: 현재 시각, deletedBy: 헤더 사용자 ID
     */
    // 공통 삭제 로직: 문자열 userId를 받아 소프트 삭제 수행
    @Transactional
    public void deleteByUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new UserNotFoundException();
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);
        user.delete(uuid);
        userRepository.save(user);
    }
}

package com.tunaforce.auth.service;

import com.tunaforce.auth.entity.User;
import com.tunaforce.auth.exception.UserNotFoundException;
import com.tunaforce.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Transactional
    public void removeAccountByHeader(String headerUserId) {
        if (headerUserId == null || headerUserId.isBlank()) {
            throw new UserNotFoundException();
        }
        UUID userId;
        try {
            userId = UUID.fromString(headerUserId);
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 소프트 삭제 마킹
        user.delete(userId);

        // 업데이트 저장
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        UUID uid = UUID.fromString(userId);
        User user = userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
        user.delete(uid);
        userRepository.save(user);
    }
}

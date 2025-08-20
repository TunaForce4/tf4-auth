package com.tunaforce.auth.service;

import com.tunaforce.auth.entity.User;
import com.tunaforce.auth.exception.UserNotFoundException;
import com.tunaforce.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Value("${service.jwt.secret-key}")
    private String secret;

    /**
     * JWT 토큰에서 사용자 ID를 추출해 해당 사용자에 대해 소프트 삭제 수행
     * - deletedAt: 현재 시각, deletedBy: 토큰 사용자 ID
     */
    @Transactional
    public void removeAccount(String token) {
        UUID userId = extractUserId(token);
        if (userId == null) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 소프트 삭제 마킹
        user.delete(userId);

        // 업데이트 저장
        userRepository.save(user);
    }

    private UUID extractUserId(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));
        String id = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", String.class);
        try {
            return id == null ? null : UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(UserNotFoundException::new);

    @Transactional
    public void deleteUser(String userId) {
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

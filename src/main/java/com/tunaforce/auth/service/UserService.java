package com.tunaforce.auth.service;

import com.tunaforce.auth.dto.request.UserInfoEditRequestDto;
import com.tunaforce.auth.entity.User;
import com.tunaforce.auth.entity.UserRole;
import com.tunaforce.auth.exception.ForbiddenException;
import com.tunaforce.auth.exception.UserNotFoundException;
import com.tunaforce.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    }

    @Transactional
    public void editUserInfo(UserInfoEditRequestDto dto, String userId, String role) {
        // 권한 체크: role이 null이거나 MASTER가 아니면 403
        if (role == null || role.isBlank()) {
            throw new ForbiddenException();
        }
        UserRole requesterRole;
        try {
            requesterRole = UserRole.fromString(role);
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException();
        }
        if (requesterRole != UserRole.MASTER) {
            throw new ForbiddenException();
        }

        // 대상 사용자 조회
        UUID uuid;
        try {
            uuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);

        // DTO 매핑 및 업데이트: 비밀번호는 제공 시 인코딩
        String encodedPassword = null;
        if (dto.password() != null && !dto.password().isBlank()) {
            encodedPassword = passwordEncoder.encode(dto.password());
        }
        UserRole newRole = null;
        if (dto.role() != null && !dto.role().isBlank()) {
            try {
                newRole = UserRole.fromString(dto.role());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 역할입니다: " + dto.role());
            }
        }
        user.updateInfo(dto.name(), encodedPassword, newRole, dto.slackId(), dto.tel());
    }
}

package com.tunaforce.auth.service;

import com.tunaforce.auth.dto.request.UserInfoEditRequestDto;
import com.tunaforce.auth.dto.response.UserInfoListResponseDto;
import com.tunaforce.auth.dto.response.UserInfoResponseDto;
import com.tunaforce.auth.entity.User;
import com.tunaforce.auth.entity.UserRole;
import com.tunaforce.auth.exception.ForbiddenException;
import com.tunaforce.auth.exception.UserNotFoundException;
import com.tunaforce.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 삭제: 대상(target) 또는 요청자(requester)가 동일하면 허용, 아니면 MASTER만 허용
     * deletedBy는 요청자 ID로 기록
     */
    @Transactional
    public void deleteUser(UUID targetUserId, UUID requesterUserId, UserRole requesterRole) {
        if (requesterUserId == null) throw new ForbiddenException();

        if (checkSelfOrMaster(targetUserId, requesterUserId, requesterRole)) {
            throw new ForbiddenException();
        }

        UUID target = (targetUserId == null) ? requesterUserId : targetUserId;
        User user = userRepository.findById(target).orElseThrow(UserNotFoundException::new);
        user.delete(requesterUserId);
    }

    @Transactional
    public void editUserInfo(UserInfoEditRequestDto dto, UUID userId, UserRole role) {
        // MASTER만 수정 허용
        requireMaster(role);

        // 대상 사용자 조회
        if (userId == null) throw new UserNotFoundException();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

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

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(UUID userId, UUID headerUserId, UserRole headerRoles) {
        // 헤더 사용자 ID 필수 (UUID라 공백 체크 불필요)
        if (headerUserId == null) {
            throw new ForbiddenException();
        }

        if (checkSelfOrMaster(userId, headerUserId, headerRoles)) {
            throw new ForbiddenException();
        }

        return UserInfoResponseDto.from(
                userRepository.findById(userId).orElseThrow(UserNotFoundException::new)
        );
    }

    @Transactional(readOnly = true)
    public UserInfoListResponseDto searchUsers(String name, UserRole headerRoles) {
        // MASTER만 접근
        requireMaster(headerRoles);

        String normalizedName = (name == null || name.isBlank()) ? null : name;

        List<User> users = (normalizedName == null)
                ? userRepository.findByDeletedAtIsNull()
                : userRepository.findByUsernameContainingIgnoreCaseAndDeletedAtIsNull(normalizedName);

        List<UserInfoResponseDto> data = users.stream()
                .map(UserInfoResponseDto::from)
                .toList();

        return new UserInfoListResponseDto(data);
    }

    // ===== Helpers =====
    private void requireMaster(UserRole role) {
        if (role != UserRole.MASTER) throw new ForbiddenException();
    }

    private boolean checkSelfOrMaster(UUID targetUserId, UUID requesterUserId, UserRole requesterRole) {
        boolean isSelf = targetUserId != null && targetUserId.equals(requesterUserId);
        boolean isMaster = requesterRole == UserRole.MASTER;
        return !isSelf && !isMaster;
    }
}

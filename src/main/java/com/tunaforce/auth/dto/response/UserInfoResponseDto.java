package com.tunaforce.auth.dto.response;

import com.tunaforce.auth.entity.User;

import java.util.UUID;

public record UserInfoResponseDto(
        UUID userId,
        String userLoginId,
        String username,
        String role,
        String slackId,
        String tel
) {
    public static UserInfoResponseDto from(User user) {
        return new UserInfoResponseDto(
                user.getUserId(),
                user.getUserLoginId(),
                user.getUsername(),
                user.getRole().name(),   // Enum → String
                user.getSlackId(),
                user.getTel()
        );
    }
}

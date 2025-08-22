package com.tunaforce.auth.dto.request;

import com.tunaforce.auth.entity.UserRole;

public record UserInfoEditRequestDto(
        String name,
        String password,
        UserRole role,
        String slackId,
        String tel
) {
}

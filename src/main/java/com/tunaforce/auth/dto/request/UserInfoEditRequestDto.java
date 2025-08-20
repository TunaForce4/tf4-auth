package com.tunaforce.auth.dto.request;

public record UserInfoEditRequestDto(
        String name,
        String password,
        String role,
        String slackId,
        String tel
) {
}

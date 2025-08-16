package com.tunaforce.auth.dto.request;

public record LoginRequestDto(
        String userLoginId,
        String password
) {
}

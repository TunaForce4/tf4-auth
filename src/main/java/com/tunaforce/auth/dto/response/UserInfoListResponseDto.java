package com.tunaforce.auth.dto.response;

import java.util.List;

public record UserInfoListResponseDto(
        List<UserInfoResponseDto> data
) {
}

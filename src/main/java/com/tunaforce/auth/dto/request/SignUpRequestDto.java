package com.tunaforce.auth.dto.request;

import com.tunaforce.auth.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 회원가입 요청 DTO
 *
 * JSON 예시
 * {
 *   "name": "본명",
 *   "userLoginId": "id",
 *   "password": "비밀번호",
 *   "role": "MASTER | COMPANY | HUB | DELIVERY",
 *   "deptId": "업체/허브 ID (nullable)",
 *   "slackId": "슬랙Id (nullable)",
 *   "tel": "전화번호"
 * }
 */
public record SignUpRequestDto(
	@NotBlank String name,
	@NotBlank String userLoginId,
	@NotBlank String password,
	@NotNull UserRole role,
	String deptId,
	String slackId,
	@NotBlank String tel
) {
}

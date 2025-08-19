package com.tunaforce.auth.dto.request;

import com.tunaforce.auth.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


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
	@NotBlank @Pattern(regexp = "^.{8,}$", message = "비밀번호는 최소 8자 이상이어야합니다.") String password,
	@NotNull UserRole role,
	String slackId,
	@NotBlank String tel
) {
}

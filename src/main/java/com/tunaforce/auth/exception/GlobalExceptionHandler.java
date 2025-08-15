package com.tunaforce.auth.exception;

import com.tunaforce.auth.dto.response.FieldErrorDetail;
import com.tunaforce.auth.dto.response.ValidationErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 전역 예외 처리기
 *
 * - 컨트롤러에서 발생하는 검증/도메인 예외를 한 곳에서 잡아
 *   표준화된 에러 바디(ValidationErrorResponse)로 변환합니다.
 * - 현재 프로젝트 정책상 HTTP 상태 코드를 442로 사용합니다(비표준 코드).
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 스프링 바인딩/검증(@Valid) 실패 시 던지는 예외를 처리합니다.
     * 필드 단위 오류들을 수집해 errors 배열에 담아 반환합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // BindingResult에 누적된 FieldError 목록을 커스텀 DTO로 변환
        List<FieldErrorDetail> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toDetail)
                .collect(Collectors.toList());

        // 요청 사양에 맞는 응답 바디 구성
        ValidationErrorResponse body = new ValidationErrorResponse(
                442,
                "VALIDATION_FAILED",
                "입력값이 유효하지 않습니다.",
                details
        );
        // 프로젝트 정책에 따라 상태 코드 442로 반환(표준 400/422 대신)
        return ResponseEntity.status(442).body(body);
    }

    /**
     * 비밀번호 전용 도메인 검증 예외 처리
     */
    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ValidationErrorResponse> handlePassword(PasswordException ex) {
        ValidationErrorResponse body = new ValidationErrorResponse(
                442,
                "VALIDATION_FAILED",
                ex.getMessage(),
                ex.getErrors()
        );
        return ResponseEntity.status(442).body(body);
    }

    /**
     * 스프링의 FieldError를 API 응답용 FieldErrorDetail로 변환합니다.
     * rejectedValue는 null/빈문자 등 원본 값을 그대로 담습니다.
     */
    private FieldErrorDetail toDetail(FieldError fe) {
        Object rejected = fe.getRejectedValue();
        return new FieldErrorDetail(
                fe.getField(),
                rejected,
                fe.getDefaultMessage()
        );
    }
}

package com.tunaforce.auth.exception;

import com.tunaforce.auth.dto.response.FieldErrorDetail;
import lombok.Getter;

import java.util.List;

/**
 * 비밀번호 검증 전용 예외
 */
@Getter
public class PasswordException extends RuntimeException {
    private final List<FieldErrorDetail> errors;

    public PasswordException(String message, List<FieldErrorDetail> errors) {
        super(message);
        this.errors = errors;
    }

    public PasswordException(String message, String rejectedValue, String fieldMessage) {
        super(message);
        this.errors = List.of(new FieldErrorDetail("password", rejectedValue, fieldMessage));
    }

}

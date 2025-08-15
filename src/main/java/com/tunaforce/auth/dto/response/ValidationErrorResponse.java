package com.tunaforce.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationErrorResponse {
    private final int code;
    private final String error;
    private final String message;
    private final List<FieldErrorDetail> errors;
}

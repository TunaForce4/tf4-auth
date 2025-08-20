package com.tunaforce.auth.dto.response;

import java.util.List;

public record ValidationErrorResponse(
        int code,
        String error,
        String message,
        List<FieldErrorDetail> errors
){}
package com.tunaforce.auth.dto.response;

public record FieldErrorDetail(String field, Object rejectedValue, String message) {
}

package com.tunaforce.auth.exception;

/**
 * 권한 없음(403) 예외
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("접근 권한이 없습니다.");
    }
}

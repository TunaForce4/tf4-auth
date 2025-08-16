package com.tunaforce.auth.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("요청하신 회원을 찾을 수 없습니다.");
    }
}

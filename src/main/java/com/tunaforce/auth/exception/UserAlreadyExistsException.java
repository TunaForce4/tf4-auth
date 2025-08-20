package com.tunaforce.auth.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("이미 등록된 id입니다.");
    }
}

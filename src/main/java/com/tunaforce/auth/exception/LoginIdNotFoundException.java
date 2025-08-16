package com.tunaforce.auth.exception;

public class LoginIdNotFoundException extends RuntimeException {
    public LoginIdNotFoundException(String message) {
        super("User Id not found: " + message);
    }
}

package com.pwoj.as.account.domain.exception;

public class UserTooYoungException extends RuntimeException {
    public UserTooYoungException(String message) {
        super(message);
    }
}

package com.pwoj.as.account.domain.exception;

public class AccountExistsException extends RuntimeException {
    public AccountExistsException(String message) {
        super(message);
    }
}

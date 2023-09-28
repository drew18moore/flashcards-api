package com.drewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}

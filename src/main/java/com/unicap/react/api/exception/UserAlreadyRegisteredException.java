package com.unicap.react.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyRegisteredException extends ResponseStatusException {
    public UserAlreadyRegisteredException() {
        super(HttpStatus.UNAUTHORIZED, "E-mail já cadastrado.");
    }
}

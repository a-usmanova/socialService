package ru.skillbox.diplom.group32.social.service.config.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT, reason = "This email already taken")
public class UserAlreadyExistsException extends AuthenticationException {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}

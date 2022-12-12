package ru.skillbox.diplom.group32.social.service.config.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Passwords are not matching")
public class PasswordsAreNotMatchingException extends AuthenticationException {
    public PasswordsAreNotMatchingException(String msg) {
        super(msg);
    }
}

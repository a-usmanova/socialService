package ru.skillbox.diplom.group32.social.service.config.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFoundException extends UsernameNotFoundException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}

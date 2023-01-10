package ru.skillbox.diplom.group32.social.service.controllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice
public class DefaultControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public Object entityNotFound(EntityNotFoundException e) {
        log.error("PasswordRecoveryExceptionHandler.entityNotFound: Запрос на изменение пароля с невалидным идентификатором, {}", e.getMessage());
        return new Object() {
            public String message = "Невалидный идентификатор";
        };
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Object defaultExceptionHandler(RuntimeException e) {
        return new Object() {
            public String message = "Ошибка запроса";
        };
    }
}

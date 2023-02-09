package ru.skillbox.diplom.group32.social.service.controllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.skillbox.diplom.group32.social.service.config.security.exception.WrongPasswordException;

@Slf4j
@ControllerAdvice
public class DefaultControllerAdvice {


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Object defaultExceptionHandler(RuntimeException e) {

        log.info("Exception!: " + e);


        return new Object() {
            public String message = "Ошибка запроса";
        };

    }

    @ExceptionHandler(ResponseStatusException.class)
    public Object responseStatusExceptionHandler(ResponseStatusException e) {
        throw e;
    }

    @ExceptionHandler(WrongPasswordException.class)
    public Object wrongPasswordExceptionHandler(WrongPasswordException e) {
        throw e;
    }
}

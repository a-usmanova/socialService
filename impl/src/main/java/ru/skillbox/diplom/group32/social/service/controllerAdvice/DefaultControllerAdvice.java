package ru.skillbox.diplom.group32.social.service.controllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.skillbox.diplom.group32.social.service.config.security.exception.*;
import ru.skillbox.diplom.group32.social.service.model.error.ErrorDto;

import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DefaultControllerAdvice {

    private final ErrorResponseEntityProducer errorResponseEntityProducer;

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public Object defaultExceptionHandler(RuntimeException e) {
        log.info("DefaultControllerAdvice.defaultExceptionHandler: handle exception: " + e, e);
        return errorResponseEntityProducer.produceErrorDto(HttpStatus.BAD_REQUEST, "Ошибка запроса");
    }

    @ResponseBody
    @ExceptionHandler(ResponseStatusException.class)
    public Object responseStatusExceptionHandler(ResponseStatusException e) {
        log.info("DefaultControllerAdvice.responseStatusExceptionHandler: handle exception: " + e, e);
        return errorResponseEntityProducer.produceErrorDto(e.getStatus(), e.getReason());
    }

    @ResponseBody
    @ExceptionHandler(value = {
            UserNotFoundException.class,
            UserAlreadyExistsException.class,
            WrongPasswordException.class,
            WrongCaptchaException.class,
            PasswordsAreNotMatchingException.class
    })
    public Object customExceptionHandler(Exception e) {
        log.info("DefaultControllerAdvice.customExceptionHandler: handle exception: " + e, e);
        return errorResponseEntityProducer.produceErrorDto(e);
    }
}

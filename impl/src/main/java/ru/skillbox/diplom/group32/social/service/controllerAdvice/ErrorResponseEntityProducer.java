package ru.skillbox.diplom.group32.social.service.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.skillbox.diplom.group32.social.service.model.error.ErrorDto;

import java.time.ZonedDateTime;

@Component
public class ErrorResponseEntityProducer {

    /**
     * Retrieves httpStatus and reason from ResponseStatus annotation, and produces errorDto
     * @param e
     * @return
     */
    public ResponseEntity<ErrorDto> produceErrorDto(Exception e) {

        HttpStatus httpStatus = e.getClass().getAnnotation(ResponseStatus.class).value();
        String errorDescription = e.getClass().getAnnotation(ResponseStatus.class).reason();

        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(httpStatus.value());
        errorDto.setErrorDescription(errorDescription);
        errorDto.setError(httpStatus.getReasonPhrase());
        errorDto.setTimestamp(ZonedDateTime.now());

        return new ResponseEntity<>(errorDto, httpStatus);
    }

    public ResponseEntity<ErrorDto> produceErrorDto(HttpStatus httpStatus, String errorDescription) {

        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(httpStatus.value());
        errorDto.setErrorDescription(errorDescription);
        errorDto.setError(httpStatus.getReasonPhrase());
        errorDto.setTimestamp(ZonedDateTime.now());
        
        return new ResponseEntity<>(errorDto, httpStatus);
    }

}

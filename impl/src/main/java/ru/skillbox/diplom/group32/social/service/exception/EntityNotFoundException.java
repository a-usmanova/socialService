package ru.skillbox.diplom.group32.social.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = EntityNotFoundException.REASON_EN)
public class EntityNotFoundException extends javax.persistence.EntityNotFoundException {

    public static final String REASON_RU = "Сущность не найдена в бд";
    public static final String REASON_EN = "Entity not found in DB";

}

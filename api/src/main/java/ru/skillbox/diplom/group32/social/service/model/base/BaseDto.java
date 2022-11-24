package ru.skillbox.diplom.group32.social.service.model.base;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseDto implements Serializable {

    private Long id;

    private Boolean isDeleted;


}

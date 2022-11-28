package ru.skillbox.diplom.group32.social.service.model.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class BaseDto implements Serializable {

    private Long id;

    private Boolean isDeleted;


}

package ru.skillbox.diplom.group32.social.service.model.base;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class BaseDto {

    private Long id;

}

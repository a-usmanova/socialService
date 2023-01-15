package ru.skillbox.diplom.group32.social.service.model.base;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSearchDto implements Serializable {

    private Long id;

    private Boolean isDeleted = false;

}

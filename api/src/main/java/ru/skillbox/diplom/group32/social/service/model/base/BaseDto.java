package ru.skillbox.diplom.group32.social.service.model.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Schema(description = "Базовый ДТО")
public abstract class BaseDto implements Serializable {
    @Schema(description = "Автоматически сгенерированный идентификатор", example = "42352")
    private Long id;
    @Schema(description = "Объект удален?", example = "false")
    private Boolean isDeleted;


}

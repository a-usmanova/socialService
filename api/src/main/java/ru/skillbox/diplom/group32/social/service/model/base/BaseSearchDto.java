package ru.skillbox.diplom.group32.social.service.model.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Базовое ДТО поиска")
public abstract class BaseSearchDto implements Serializable {
    @Schema(description = "Идентификатор для поиска", example = "42352")
    private Long id;
    @Schema(description = "Флаг удаления объекта поиска", example = "false")
    private Boolean isDeleted = false;

}

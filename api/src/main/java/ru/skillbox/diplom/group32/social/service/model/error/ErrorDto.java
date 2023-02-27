package ru.skillbox.diplom.group32.social.service.model.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Schema(description = "Дто для тела ответа при ошибке запроса")
public class ErrorDto {

    @Schema(description = "Описание ошибки")
    @JsonProperty("error_description")
    public String errorDescription;

    @Schema(description = "Время ошибки")
    public ZonedDateTime timestamp;

    @Schema(description = "Код статуса ответа")
    public Integer status;

    @Schema(description = "Название статуса ответа")
    public String error;
}

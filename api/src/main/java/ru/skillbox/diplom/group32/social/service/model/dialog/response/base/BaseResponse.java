package ru.skillbox.diplom.group32.social.service.model.dialog.response.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Базовый респонс")
public abstract class BaseResponse {
    protected BaseResponse(String error, String errorDescription, Long timestamp) {
        this.error = error;
        this.errorDescription = errorDescription;
        this.timestamp = timestamp;
    }
    @Schema(description = "Ошибка по запросу", example = "Неверный запрос")
    private String error;

    @Schema(description = "Описание ошибки", example = "Неверный код авторизации")
    private String errorDescription;

    @Schema(description = "Метка времени", example = "1644234125")
    private Long timestamp;
}

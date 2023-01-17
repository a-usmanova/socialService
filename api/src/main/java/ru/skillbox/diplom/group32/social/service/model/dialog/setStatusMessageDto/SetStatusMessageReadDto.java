package ru.skillbox.diplom.group32.social.service.model.dialog.setStatusMessageDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто ответа на вопрос 'Проверить сообщение как прочитанное'")
public class SetStatusMessageReadDto{
    public SetStatusMessageReadDto(String message) {

        this.message = message;

    }
    @Schema(description = "Сообщение о выполнении", example = "ок")
    private String message;

}


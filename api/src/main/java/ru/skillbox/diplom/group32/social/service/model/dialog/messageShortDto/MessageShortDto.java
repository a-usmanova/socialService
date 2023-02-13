package ru.skillbox.diplom.group32.social.service.model.dialog.messageShortDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто сообщения краткое")
public class MessageShortDto extends BaseDto {

    @Schema(description = "Дата и время отправки", example = "1673667157")
    private Long time;
    @Schema(description = "Id автора сообщения")
    private Long authorId;
    @Schema(description = "Текст сообщения")
    private String messageText;

}

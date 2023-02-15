package ru.skillbox.diplom.group32.social.service.model.dialog.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто сообщения")
public class MessageDto extends BaseDto {

    @Schema(description = "Дата и время отправки", example = "1673667157")
    private ZonedDateTime time;
    @Schema(description = "Id автора сообщения")
    private Long authorId;
    @Schema(description = "Id получателя сообщения")
    private Long recipientId;
    @Schema(description = "Текст сообщения")
    private String messageText;
    @Schema(description = "Статус прочтения: SENT, READ - отправлен, прочитан")
    private ReadStatusDto readStatusDto;
    @Schema(description = "Id диалога")
    private Long dialogId;

}

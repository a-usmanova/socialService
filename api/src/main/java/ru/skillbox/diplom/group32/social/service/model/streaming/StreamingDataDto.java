package ru.skillbox.diplom.group32.social.service.model.streaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Дто стриминг сообщения")
public class StreamingDataDto {

    @Schema(description = "Id сообщения")
    private Long id;
    @Schema(description = "Дата и время отправки в millis")
    private Long time;
    @Schema(description = "Id автора сообщения")
    private Long authorId;
    @Schema(description = "Id получателя сообщения")
    private Long recipientId;
    @Schema(description = "Текст сообщения")
    private String messageText;

}

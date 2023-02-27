package ru.skillbox.diplom.group32.social.service.model.streaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Дто содержания стриминг сообщения")
public class StreamingDataDto {

    @Schema(description = "Id сообщения", example = "31131")
    private Long id;
    @Schema(description = "Дата и время отправки в millis", example = "1677445332662")
    private Long time;
    @Schema(description = "Id автора сообщения", example = "13231")
    private Long authorId;
    @Schema(description = "Id получателя сообщения", example = "5235")
    private Long recipientId;
    @Schema(description = "Текст сообщения", example = "Hello fellow kid")
    private String messageText;

}

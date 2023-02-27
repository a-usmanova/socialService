package ru.skillbox.diplom.group32.social.service.model.streaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Дто  стриминг сообщения")
public class StreamingMessageDto<T>{
    @Schema(description = "Тип сообщения", example = "MESSAGE")
    private String type;
    @Schema(description = "Id целевого аккаунта", example = "31131")
    private Long accountId;
    @Schema(description = "Содержание сообщения", example = "StreamingDataDto: [3131, 1677445332662, 13212, 32131, Hello fellow kid]")
    private T data;

}

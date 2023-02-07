package ru.skillbox.diplom.group32.social.service.model.dialog.unreadCountDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто количества непрочитанных сообщений")
public class UnreadCountDto {

    public UnreadCountDto(Long count) {
        this.count = count;
    }
    @Schema(description = "Количество непрочитанных сообщений в диалоге")
    private Long count;

}

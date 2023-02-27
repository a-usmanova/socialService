package ru.skillbox.diplom.group32.social.service.model.like;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто для лайка")
public class LikeDto extends BaseDto {
    private Long authorId;
    @Schema(description = "Время создания лайка")

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime time;
    @Schema(description = "id поста или комментария, которому поставлен лайк")
    private Long itemId;
    @Schema(description = "Тип лайка: POST, COMMENT- лайк на пост, лайк на комментарий")
    private LikeType type;
}

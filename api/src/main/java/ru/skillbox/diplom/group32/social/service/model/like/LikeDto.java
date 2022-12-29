package ru.skillbox.diplom.group32.social.service.model.like;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
public class LikeDto extends BaseDto {
    private Long authorId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime time;
    private Long itemId;
    private LikeType type;
}

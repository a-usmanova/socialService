package ru.skillbox.diplom.group32.social.service.model.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Schema(description = "Дто для поста")
public class PostDto extends BaseDto {

    @Schema(description = "Время создания поста")
    private ZonedDateTime time;

    @Schema(description = "Время изменения поста")
    private ZonedDateTime timeChanged;

    @Schema(description = "Айди автора поста")
    private Long authorId;

    @Schema(description = "Заголовок")
    private String title;

    @Schema(description = "Тип поста: POSTED, QUEUED - опубликован, поставлен в очередь (для отложенной публикации)")
    private Type type;

    @Schema(description = "Текст поста")
    private String postText;

    @Schema(description = "Заблокирован?")
    private Boolean isBlocked;

    @Schema(description = "Удален?")
    private Boolean isDeleted;

    @Schema(description = "Количество комментов")
    private Long commentsCount;

    @Schema(description = "Теги")
    private Set<String> tags = new HashSet();

    @Schema(description = "Количество лайков")
    private Long likeAmount;

    @Schema(description = "Мой лайк?")
    private Boolean myLike;

    @Schema(description = "Путь к фото")
    private String imagePath;

    @Schema(description = "Время публикации поста")
    private ZonedDateTime publishDate;

}

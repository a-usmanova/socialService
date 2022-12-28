package ru.skillbox.diplom.group32.social.service.model.post.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;
@Data
@AllArgsConstructor
@Schema(description = "Дто для коммента")
public class CommentDto extends BaseDto {

      @Schema(description = "Тип коммента - POST, COMMENT - если POST - коммент к посту, если COMMENT - коммент к комменту, субкоммент")
      private CommentType commentType;

      @Schema(description = "Время создания коммента")
      private ZonedDateTime time;

      @Schema(description = "Время обновления коммента")
      private ZonedDateTime timeChanged;

      @Schema(description = "Айди автора коммента")
      private Long authorId;

      @Schema(description = "Айди коммента-родителя, к которому оставлен коммент")
      private Long parentId;

      @Schema(description = "Текст коммента")
      private String commentText;

      @Schema(description = "Айди поста")
      private Long postId;

      @Schema(description = "Заблокирован?")
      private Boolean isBlocked;

      @Schema(description = "Удален?")
      private Boolean isDeleted;

      @Schema(description = "Количество лайков")
      private Long likeAmount;

      @Schema(description = "Мой лайк?")
      private Boolean myLike;

      @Schema(description = "Количество комментов")
      private Long commentsCount;

      @Schema(description = "Адрес картинки")
      private String imagePath;


}

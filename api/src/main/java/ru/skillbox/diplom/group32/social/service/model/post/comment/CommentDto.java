package ru.skillbox.diplom.group32.social.service.model.post.comment;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
public class CommentDto extends BaseDto {

      private CommentType commentType;
      private ZonedDateTime time;
      private ZonedDateTime timeChanged;
      private Long authorId;
      private Long parentId;
      private String commentText;
      private Long postId;
      private Boolean isBlocked;
      private Boolean isDeleted;
      private Long likeAmount;
      private Boolean myLike;
      private Long commentsCount;
      private String imagePath;


}

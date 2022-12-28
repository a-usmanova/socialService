package ru.skillbox.diplom.group32.social.service.model.post.comment;

import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.time.ZonedDateTime;

@Data
public class CommentSearchDto extends BaseSearchDto {

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

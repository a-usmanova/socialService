package ru.skillbox.diplom.group32.social.service.model.post.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

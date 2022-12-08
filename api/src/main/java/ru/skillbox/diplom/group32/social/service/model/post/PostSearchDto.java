package ru.skillbox.diplom.group32.social.service.model.post;

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
public class PostSearchDto extends BaseSearchDto {

    private ZonedDateTime time;
    private ZonedDateTime timeChanged;
    private Integer authorId;
    private String title;
    private Type type;
    private String postText;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private Integer commentsCount;
    private String[] tags;
    private Integer likeAmount;
    private Boolean myLike;
    private String imagePath;
    private ZonedDateTime publishDate;

}

package ru.skillbox.diplom.group32.social.service.model.post;

import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;

@Data
public class PostDto extends BaseDto {

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

package ru.skillbox.diplom.group32.social.service.model.post;

import lombok.Data;
import org.aspectj.apache.bcel.generic.Tag;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;
import ru.skillbox.diplom.group32.social.service.model.tag.TagDto;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class PostDto extends BaseDto {

    private ZonedDateTime time;
    private ZonedDateTime timeChanged;
    private Long authorId;
    private String title;
    private Type type;
    private String postText;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private Long commentsCount;
    private Set<String> tags = new HashSet();
    private Long likeAmount;
    private Boolean myLike;
    private String imagePath;
    private ZonedDateTime publishDate;

}

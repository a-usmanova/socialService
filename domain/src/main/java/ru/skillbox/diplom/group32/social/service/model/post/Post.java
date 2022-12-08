package ru.skillbox.diplom.group32.social.service.model.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Table(name = "post")
public class Post extends BaseEntity {

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime time;

    @LastModifiedDate
    @Column(name = "time_changed", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime timeChanged;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "comments_count")
    private Integer commentsCount;

//    @Column(nullable = false)
//    private List<Tag> tags;

    @Column(name = "like_amount")
    private Integer likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "publish_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime publishDate;

}

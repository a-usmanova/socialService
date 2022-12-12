package ru.skillbox.diplom.group32.social.service.model.post.comment;

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
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_type")
    private CommentType commentType;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime time;

    @LastModifiedDate
    @Column(name = "time_changed", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime timeChanged;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "like_amount")
    private Long likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;

    @Column(name = "comments_count")
    private Long commentsCount;

    @Column(name = "image_path")
    private String imagePath;

}

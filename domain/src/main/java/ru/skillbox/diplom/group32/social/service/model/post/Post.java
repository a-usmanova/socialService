package ru.skillbox.diplom.group32.social.service.model.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private Long authorId;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "comments_count")
    private Long commentsCount;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet();

    @Column(name = "like_amount")
    private Long likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "publish_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime publishDate;

}

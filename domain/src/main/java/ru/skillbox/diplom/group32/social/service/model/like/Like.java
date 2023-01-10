package ru.skillbox.diplom.group32.social.service.model.like;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Like extends BaseEntity {
    @Column(name = "author_id", nullable = false)
    private Long authorId;
    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private ZonedDateTime time;
    @Column(name = "item_id")
    private Long itemId;
    @Enumerated(EnumType.STRING)
    private LikeType type;
}

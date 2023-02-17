package ru.skillbox.diplom.group32.social.service.model.notification;

import lombok.*;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class UserNotification extends BaseEntity implements Cloneable {

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime time;

    @Column(name = "content")
    private String content;

    public UserNotification clone() {
        try {
            return (UserNotification) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}

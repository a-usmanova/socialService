package ru.skillbox.diplom.group32.social.service.model.notification;

import lombok.*;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_settings")
public class NotificationSettings extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post")
    private Boolean post = true;

    @Column(name = "post_comment")
    private Boolean postComment = true;

    @Column(name = "comment_comment")
    private Boolean commentComment = true;

    @Column(name = "message")
    private Boolean message = true;

    @Column(name = "friend_request")
    private Boolean friendRequest = true;

    @Column(name = "friend_birthday")
    private Boolean friendBirthday = true;

    @Column(name = "send_email_message")
    private Boolean sendEmailMessage = false;
}

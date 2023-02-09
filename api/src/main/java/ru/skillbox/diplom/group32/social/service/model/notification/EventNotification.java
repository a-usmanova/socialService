package ru.skillbox.diplom.group32.social.service.model.notification;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class EventNotification {
    private Long authorId;
    private Long receiverId;
    private NotificationType notificationType;
    private String content;
}

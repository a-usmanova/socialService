package ru.skillbox.diplom.group32.social.service.model.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventNotification {
    private Long authorId;
    private Long receiverId;
    private NotificationType notificationType;
    private String content;
}

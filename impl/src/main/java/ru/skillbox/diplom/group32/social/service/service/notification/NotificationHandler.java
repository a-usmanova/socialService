package ru.skillbox.diplom.group32.social.service.service.notification;

import ru.skillbox.diplom.group32.social.service.model.notification.NotificationType;
import ru.skillbox.diplom.group32.social.service.model.notification.UserNotification;

import java.util.List;

public interface NotificationHandler {
    List<UserNotification> fillReceiverIds(UserNotification userNotification);

    NotificationType myType();
}

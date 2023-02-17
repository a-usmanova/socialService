package ru.skillbox.diplom.group32.social.service.service.notification.notificationHandlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationType;
import ru.skillbox.diplom.group32.social.service.model.notification.UserNotification;
import ru.skillbox.diplom.group32.social.service.repository.notification.NotificationSettingsRepository;
import ru.skillbox.diplom.group32.social.service.service.notification.NotificationHandler;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageNotificationHandler implements NotificationHandler {

    private final NotificationSettingsRepository settingsRepository;
    @Override
    public List<UserNotification> fillReceiverIds(UserNotification userNotification) {
        List<UserNotification> list = new ArrayList<>();
        if (settingsRepository.findByUserId(userNotification.getReceiverId()).orElseThrow().getMessage())
            list.add(userNotification);
        return list;
    }

    @Override
    public NotificationType myType() {
        return NotificationType.MESSAGE;
    }
}

package ru.skillbox.diplom.group32.social.service.service.notification.notificationHandlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationType;
import ru.skillbox.diplom.group32.social.service.model.notification.UserNotification;
import ru.skillbox.diplom.group32.social.service.repository.friend.FriendRepository;
import ru.skillbox.diplom.group32.social.service.repository.notification.NotificationSettingsRepository;
import ru.skillbox.diplom.group32.social.service.service.friend.FriendService;
import ru.skillbox.diplom.group32.social.service.service.notification.NotificationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostNotificationHandler implements NotificationHandler {
    private final NotificationSettingsRepository settingsRepository;
    private final FriendService friendService;
    @Override
    public List<UserNotification> fillReceiverIds(UserNotification userNotification) {
        Long authorId = userNotification.getAuthorId();
        List<Long> friendsIds = friendService.getFriendsIds(authorId);
        List<UserNotification> notificationList = friendsIds.stream().filter(id -> settingsRepository.findByUserId(id).orElseThrow().getPost()).map(id -> {
            UserNotification notification = userNotification.clone();
            notification.setReceiverId(id);
            return notification;
        }).collect(Collectors.toList());

        return notificationList;
    }

    @Override
    public NotificationType myType() {
        return NotificationType.POST;
    }
}

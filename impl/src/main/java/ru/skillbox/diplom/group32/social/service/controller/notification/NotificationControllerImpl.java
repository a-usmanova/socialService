package ru.skillbox.diplom.group32.social.service.controller.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.notification.*;
import ru.skillbox.diplom.group32.social.service.resource.notification.NotificationController;
import ru.skillbox.diplom.group32.social.service.service.notification.NotificationService;

@RestController
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {

    public final NotificationService notificationService;

    @Override
    public ResponseEntity<NotificationSettingsDto> getSettings() {
        return ResponseEntity.ok(notificationService.getSettings());
    }

    @Override
    public void updateSettings(NotificationSettingDto setting) {
        notificationService.updateSettings(setting);
    }

    @Override
    public ResponseEntity<NotificationsDto> getNotifcations() {
        return ResponseEntity.ok(notificationService.getNotifications());
    }

    @Override
    public ResponseEntity<NotificationCountDto> getNotificationsCount() {
        return ResponseEntity.ok(notificationService.getNotificationsCount());
    }

    @Override
    public void addNotification(@RequestBody EventNotification notification) {
        notificationService.addNotification(notification);
    }
}

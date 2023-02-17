package ru.skillbox.diplom.group32.social.service.mapper.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.notification.EventNotification;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationDto;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationSettingDto;
import ru.skillbox.diplom.group32.social.service.model.notification.UserNotification;

@Mapper
public interface UserNotificationMapper {

    @Mapping(target = "isDeleted", constant = "false")
    UserNotification toUserNotification(EventNotification notification);

    NotificationDto toNotificationDto(UserNotification userNotification);

}

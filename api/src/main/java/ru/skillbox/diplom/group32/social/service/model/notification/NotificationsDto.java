package ru.skillbox.diplom.group32.social.service.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsDto {
    private ZonedDateTime timeStamp;
    private List<NotificationDto> data;
}

package ru.skillbox.diplom.group32.social.service.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private AccountDto author;
    private String content;
    private NotificationType notificationType;
    private ZonedDateTime sentTime;
}

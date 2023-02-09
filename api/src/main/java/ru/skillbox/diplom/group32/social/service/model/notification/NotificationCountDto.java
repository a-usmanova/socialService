package ru.skillbox.diplom.group32.social.service.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCountDto {
    private Long timestamp;
    private Count data;
}

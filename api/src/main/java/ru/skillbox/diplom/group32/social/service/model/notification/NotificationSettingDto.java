package ru.skillbox.diplom.group32.social.service.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingDto {
    @JsonProperty("notification_type")
    private NotificationType notificationType;
    private Boolean enable;
}

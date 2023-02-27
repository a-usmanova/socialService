package ru.skillbox.diplom.group32.social.service.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для включения или выключения типа уведомления")
public class NotificationSettingDto {

    @JsonProperty("notification_type")
    @Schema(description = "Тип уведомления")
    private NotificationType notificationType;

    @Schema(description = "Устанавливаемое состояние")
    private Boolean enable;
}

package ru.skillbox.diplom.group32.social.service.model.notification;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;

@Schema(description = "Дто для передачи списка уведомлений")
public class NotificationSettingsDto extends ArrayList<NotificationSettingDto> {

}

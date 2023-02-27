package ru.skillbox.diplom.group32.social.service.model.notification;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Дто для уведомления")
public class NotificationDto {

    @Schema(description = "Идентификатор уведомления")
    private Long id;

    @Schema(description = "Автор уведомления")
    private AccountDto author;

    @Schema(description = "Текст уведомления")
    private String content;

    @Schema(description = "Тип уведомления")
    private NotificationType notificationType;

    @Schema(description = "Время создания уведомления")
    private ZonedDateTime sentTime;
}

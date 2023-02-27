package ru.skillbox.diplom.group32.social.service.model.notification;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Дто для передачи уведолмения")
public class NotificationsDto {

    @Schema(description = "Время запроса уведомлений")
    private ZonedDateTime timeStamp;

    @Schema(description = "Дто для уведомления")
    private List<NotificationDto> data;
}

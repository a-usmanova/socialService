package ru.skillbox.diplom.group32.social.service.model.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для передачи количества уведомлений")
public class NotificationCountDto {

    @Schema(description = "Время запроса количества уведомлений")
    private Long timestamp;

    @Schema(description = "Дто для количества уведомлений")
    private Count data;
}

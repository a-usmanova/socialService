package ru.skillbox.diplom.group32.social.service.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Дто для отслеживания аккаунта онлайн")
public class AccountOnlineDto {

    @Schema(description = "Идентификатор аккаунта")
    private Long id;
    @Schema(description = "Время последнего появления онлайн")
    private ZonedDateTime lastOnlineTime;
    @Schema(description = "Онлайн?")
    private Boolean isOnline;

}

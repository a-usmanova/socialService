package ru.skillbox.diplom.group32.social.service.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.auth.UserDto;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для аккаунта")
public class AccountDto extends UserDto {
    @Schema(description = "Телефонный номер аккаунта")
    private String phone;
    @Schema(description = "Ссылка на фото аккаунта")
    private String photo;
    @Schema(description = "Описание аккаунта")
    private String about;
    @Schema(description = "Город")
    private String city;
    @Schema(description = "Страна")
    private String country;
    @Schema(description = "StatusCode аккаунта")
    private StatusCode statusCode;
    @Schema(description = "Дата регистрации")
    private ZonedDateTime regDate;
    @Schema(description = "Дата рождения")
    private ZonedDateTime birthDate;
    @Schema(description = "Разрешения сообщения")
    private String messagePermission;
    @Schema(description = "Время последнего появления онлайн")
    private ZonedDateTime lastOnlineTime;
    @Schema(description = "Онлайн?")
    private Boolean isOnline;
    @Schema(description = "Заблокирован?")
    private Boolean isBlocked;
    @Schema(description = "Идентификатор фото")
    private String photoId;
    @Schema(description = "Имя фото")
    private String photoName;
    @Schema(description = "Дата создания аккаунта")
    private ZonedDateTime createdOn;
    @Schema(description = "Дата обновления аккаунта")
    private ZonedDateTime updatedOn;


}

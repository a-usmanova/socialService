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
    @Schema(description = "Телефонный номер аккаунта", example = "79996662233")
    private String phone;
    @Schema(description = "Ссылка на фото аккаунта", example = "http://res.cloudinary.com/duvaewonz/image/upload/v1677442010/xfaazue6lvk7ilrkkycl.jpg")
    private String photo;
    @Schema(description = "Описание аккаунта", example = "Отличное описание аккаунта")
    private String about;
    @Schema(description = "Город", example = "Москва")
    private String city;
    @Schema(description = "Страна", example = "Россия")
    private String country;
    @Schema(description = "StatusCode аккаунта", example = "StatusCode.FRIEND")
    private StatusCode statusCode;
    @Schema(description = "Дата регистрации", example = "2023-02-26 19:02:29.110138 +00:00")
    private ZonedDateTime regDate;
    @Schema(description = "Дата рождения", example = "2023-02-26 19:02:29.110138 +00:00")
    private ZonedDateTime birthDate;
    @Schema(description = "Разрешения сообщения")
    private String messagePermission;
    @Schema(description = "Время последнего появления онлайн", example = "2023-02-26 19:02:29.110138 +00:00")
    private ZonedDateTime lastOnlineTime;
    @Schema(description = "Онлайн?", example = "true")
    private Boolean isOnline;
    @Schema(description = "Заблокирован?", example = "false")
    private Boolean isBlocked;
    @Schema(description = "Идентификатор фото")
    private String photoId;
    @Schema(description = "Имя фото", example = "Аватарка v1")
    private String photoName;
    @Schema(description = "Дата создания аккаунта", example = "2023-02-26 19:02:29.110138 +00:00")
    private ZonedDateTime createdOn;
    @Schema(description = "Дата обновления аккаунта", example = "2023-02-26 19:02:29.110138 +00:00")
    private ZonedDateTime updatedOn;


}

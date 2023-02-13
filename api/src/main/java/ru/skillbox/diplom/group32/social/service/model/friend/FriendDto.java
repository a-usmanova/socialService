package ru.skillbox.diplom.group32.social.service.model.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.account.StatusCode;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@Schema(description = "Дто для друзей")
public class FriendDto extends BaseDto {

    @Schema(description = "Фото")
    private String photo;

    @Schema(description = "Статус код, состояние дружбы - в друзьях, исходящая заявка, входящая заявка, рекомендация, блокирован")
    private StatusCode statusCode;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Город")
    private String city;

    @Schema(description = "Страна")
    private String country;

    @Schema(description = "Дата рождения")
    private ZonedDateTime birthDate;

    @Schema(description = "Онлайн?")
    private Boolean isOnline;

    @Schema(name = "Аккаунт, от которого идет запрос")
    private Long fromAccountId;

    @Schema(name = "Аккаунт, к которому идет запрос")
    private Long toAccountId;
//**TODO remove previous statuscode
    @Schema(name = "Предыдущий статус")
    private StatusCode previousStatusCode;

    @Schema(name = "Рейтинг")
    private Long rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendDto friendDto = (FriendDto) o;
        return Objects.equals(rating, friendDto.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating);
    }
}

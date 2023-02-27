package ru.skillbox.diplom.group32.social.service.model.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.account.StatusCode;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.time.ZonedDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для поиска друзей")
public class FriendSearchDto extends BaseSearchDto {

    @Schema(description = "Айдишники друзей")
    private List<Long> ids;
    @Schema(description = "id от кого идет заявка")
    private Long idFrom;
    @Schema(description = "Статус - тип состояния между пользователями")
    private StatusCode statusCode;
    @Schema(description = "id кому идет заявка")
    private Long idTo;
    @Schema(description = "Имя")
    private String firstName;
    @Schema(description = "Минимальная дата рождения")
    private ZonedDateTime birthDateFrom;
    @Schema(description = "Максимальная дата рождения")
    private ZonedDateTime birthDateTo;
    @Schema(description = "Город")
    private String city;
    @Schema(description = "Страна")
    private String country;
    @Schema(description = "Возраст от")
    private Long ageFrom;
    @Schema(description = "Возраст до")
    private Long ageTo;
    @Schema(description = "Предыдущий статус код")
    private StatusCode previousStatusCode;

    public FriendSearchDto(Long idFrom, StatusCode statusCode, Long idTo) {
        this.idFrom = idFrom;
        this.statusCode = statusCode;
        this.idTo = idTo;
    }

    public FriendSearchDto(Long idFrom, StatusCode statusCode) {
        this.idFrom = idFrom;
        this.statusCode = statusCode;
    }

    public FriendSearchDto(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

}

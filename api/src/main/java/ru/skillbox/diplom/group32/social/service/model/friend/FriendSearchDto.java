package ru.skillbox.diplom.group32.social.service.model.friend;

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
public class FriendSearchDto extends BaseSearchDto {

    private List<Long> ids;
    private Long idFrom;
    private StatusCode statusCode;
    private Long idTo;
    private String firstName;
    private ZonedDateTime birthDateFrom;
    private ZonedDateTime birthDateTo;
    private String city;
    private String country;
    private Long ageFrom;
    private Long ageTo;
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

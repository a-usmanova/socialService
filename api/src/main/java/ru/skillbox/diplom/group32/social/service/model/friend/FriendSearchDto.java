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
    private Long id_from;
    private StatusCode statusCode;
    private Long id_to;
    private String firstName;
    private ZonedDateTime birthDateFrom;
    private ZonedDateTime birthDateTo;
    private String city;
    private String country;
    private Long ageFrom;
    private Long ageTo;


}

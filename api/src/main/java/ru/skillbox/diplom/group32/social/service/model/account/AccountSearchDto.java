package ru.skillbox.diplom.group32.social.service.model.account;

import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.util.List;

@Data
public class AccountSearchDto extends BaseSearchDto {

    private List<Long> ids;
    private List<Long> blockedByIds;
    private String author;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private Boolean isBlocked;
    private Integer ageTo;
    private Integer ageFrom;

}

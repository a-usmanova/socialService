package ru.skillbox.diplom.group32.social.service.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.auth.UserDto;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class AccountDto extends UserDto {

    private String phone;
    private String photo;
    private String about;
    private String city;
    private String country;
    private StatusCode statusCode;
    private ZonedDateTime regDate;
    private String messagePermission;
    private ZonedDateTime lastOnlineTime;
    private Boolean isOnline;
    private Boolean isBlocked;
    private String photoId;
    private String photoName;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;


}

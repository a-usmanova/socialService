package ru.skillbox.diplom.group32.social.service.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private int id;
    private int age;
    private String firstName;
    private String lastName;
    private String fullName;
    private ZonedDateTime birthday;

}

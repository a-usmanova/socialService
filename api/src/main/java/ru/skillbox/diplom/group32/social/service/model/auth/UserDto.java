package ru.skillbox.diplom.group32.social.service.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto { //TODO Можно в принципе сделать RegistrationDto и не привязыватьс к userDto и как в свагере ее сделать

    private String firstname;
    private String lastname;
    private String email;
    private String password;
}

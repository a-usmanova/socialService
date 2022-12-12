package ru.skillbox.diplom.group32.social.service.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    private String email;
    private String password1;
    private String password2;
    private String firstName;
    private String lastName;
    private String code;
    private String token;
}

package ru.skillbox.diplom.group32.social.service.model.auth;

import lombok.*;

@Data
@AllArgsConstructor
public class AuthenticateDto {

    private String email;
    private String password;

}

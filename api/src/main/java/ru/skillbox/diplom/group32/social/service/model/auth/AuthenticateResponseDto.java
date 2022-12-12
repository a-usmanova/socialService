package ru.skillbox.diplom.group32.social.service.model.auth;

import lombok.*;


@Data
@AllArgsConstructor
public class AuthenticateResponseDto {

    private String email;
    private String token;
}

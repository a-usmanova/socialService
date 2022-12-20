package ru.skillbox.diplom.group32.social.service.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AuthenticateResponseDto {

    private String accessToken;
    private String refreshToken;
}

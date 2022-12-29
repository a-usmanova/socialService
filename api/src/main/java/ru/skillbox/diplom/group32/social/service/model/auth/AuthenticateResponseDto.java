package ru.skillbox.diplom.group32.social.service.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(description = "Дто для ответа на аутентификацию")
public class AuthenticateResponseDto {

    @Schema(description = "Токен")
    private String accessToken;
    @Schema(description = "Токен")
    private String refreshToken;
}

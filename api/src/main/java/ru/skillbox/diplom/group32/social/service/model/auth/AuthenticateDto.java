package ru.skillbox.diplom.group32.social.service.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@Schema(description = "Дто для запроса на аутентификацию")
public class AuthenticateDto {

    @Schema(description = "Электронная почта пользователя")
    private String email;
    @Schema(description = "Пароль пользователя")
    private String password;

}

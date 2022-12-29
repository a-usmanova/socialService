package ru.skillbox.diplom.group32.social.service.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для запроса на регистрацию")
public class RegistrationDto {

    @Schema(description = "Электронная почта нового пользователя")
    private String email;
    @Schema(description = "Пароль нового пользователя")
    private String password1;
    @Schema(description = "Дубль пароля для проверки")
    private String password2;
    @Schema(description = "Фамилия")
    private String firstName;
    @Schema(description = "Имя")
    private String lastName;
    @Schema(description = "Код с изображения капчи")
    private String captchaCode;
    @Schema(description = "Идентификатор капчи")
    private String captchaSecret;
}

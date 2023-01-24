package ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для установки нового пароля")
public class NewPasswordDto {
    @Schema(description = "Пароль")
    private String password;
}

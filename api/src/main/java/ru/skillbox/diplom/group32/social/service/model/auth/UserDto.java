package ru.skillbox.diplom.group32.social.service.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для пользователя")
public class UserDto extends BaseDto {

    @Schema(description = "Фамилия", example = "Foma")
    private String firstName;
    @Schema(description = "Имя", example = "Kinyaev")
    private String lastName;
    @Schema(description = "Электронная почта пользователя", example = "fkinyaev@something.ru")
    private String email;
    @Schema(description = "Пароль пользователя", example = "Qwerty!321341")
    private String password;

}

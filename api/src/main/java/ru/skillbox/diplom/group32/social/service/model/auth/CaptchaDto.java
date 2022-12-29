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
@Schema(description = "Дто для запроса капчи")
public class CaptchaDto {

    @Schema(description = "Идентификатор капчи")
    private String secret;
    @Schema(description = "Изображение капчи закодированное в Base64")
    private String image;
}

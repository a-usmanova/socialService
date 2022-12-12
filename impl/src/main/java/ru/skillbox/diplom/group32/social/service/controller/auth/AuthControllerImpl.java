package ru.skillbox.diplom.group32.social.service.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateDto;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group32.social.service.model.auth.RegistrationDto;
import ru.skillbox.diplom.group32.social.service.resource.auth.AuthController;
import ru.skillbox.diplom.group32.social.service.service.auth.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthenticateResponseDto> login(AuthenticateDto authenticateDto) {
        return ResponseEntity.ok(authService.login(authenticateDto));
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void register(RegistrationDto registrationDto) {
//        authService.register(registrationDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void captcha() {}

    /* Можно добавить данное искючение и спринг сам преобразует все как надо
    * @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = UserNotFoundException.REASON_RU)
public class UserNotFoundException extends RuntimeException {

    public static final String REASON_RU = "Сущность пользователя не найдена в бд";
    public static final String REASON_EN = "User entity not found in db";

}*/


}

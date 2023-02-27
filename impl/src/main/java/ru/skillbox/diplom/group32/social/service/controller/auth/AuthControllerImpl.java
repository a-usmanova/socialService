package ru.skillbox.diplom.group32.social.service.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.auth.*;
import ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery.NewPasswordDto;
import ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery.PasswordRecoveryDto;
import ru.skillbox.diplom.group32.social.service.resource.auth.AuthController;
import ru.skillbox.diplom.group32.social.service.service.auth.AuthService;
import ru.skillbox.diplom.group32.social.service.service.auth.passwordRecovery.PasswordRecoveryService;
import ru.skillbox.diplom.group32.social.service.service.auth.captcha.CaptchaService;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    private final PasswordRecoveryService passwordRecoveryService;


    @Override
    public ResponseEntity<AuthenticateResponseDto> login(AuthenticateDto authenticateDto) {
        return ResponseEntity.ok(authService.login(authenticateDto));
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void logout() {}

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void register(RegistrationDto registrationDto) {
        authService.register(registrationDto);
    }

    @Override
    public ResponseEntity<CaptchaDto> captcha() {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @Override
    public void getPasswordRecoveryMail(PasswordRecoveryDto passwordRecoveryDto) {
        passwordRecoveryService.sendEmail(passwordRecoveryDto);
    }

    @Override
    public void setPassword(String linkId, NewPasswordDto newPasswordDto) {
        passwordRecoveryService.setPassword(linkId, newPasswordDto);
    }

}

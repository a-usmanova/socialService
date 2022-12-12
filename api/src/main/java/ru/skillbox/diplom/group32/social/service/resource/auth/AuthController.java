package ru.skillbox.diplom.group32.social.service.resource.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateDto;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group32.social.service.model.auth.RegistrationDto;

@RestController
@RequestMapping(value = "/api/v1/auth/")
public interface AuthController {

    @PostMapping("login")
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);

    @PostMapping("register")
    void register(@RequestBody RegistrationDto registrationDto);

    @GetMapping("captcha")
    void captcha();

    // TODO - сделать каптчу
}

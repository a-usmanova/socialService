package ru.skillbox.diplom.group32.social.service.resource.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateDto;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group32.social.service.model.auth.RegistrationDto;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = WebConstant.VERSION_URL + "/auth/")
public interface AuthController {

    @PostMapping("login")
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto, HttpServletResponse response);

    @PostMapping("logout")
    void logout();

    @PostMapping("register")
    void register(@RequestBody RegistrationDto registrationDto);

    @GetMapping("captcha")
    void captcha();

    // TODO - сделать каптчу
}

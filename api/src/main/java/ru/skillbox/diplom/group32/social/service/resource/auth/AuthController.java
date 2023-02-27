package ru.skillbox.diplom.group32.social.service.resource.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.auth.*;
import ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery.NewPasswordDto;
import ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery.PasswordRecoveryDto;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = WebConstant.VERSION_URL + "/auth/")
@Tag(name = "Auth service", description = "Аутентификация и регистрация")
public interface AuthController {

    @PostMapping("login")
    @Operation(summary = "Аутентификация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Успешная аутентификация",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthenticateResponseDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);

    @PostMapping("logout")
    @Operation(summary = "Логаут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный логаут", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    void logout();

    @PostMapping("register")
    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    void register(@RequestBody RegistrationDto registrationDto);

    @GetMapping("captcha")
    @Operation(summary = "Получение капчи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Капча получена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CaptchaDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<CaptchaDto> captcha();

    @PostMapping("password/recovery")
    @Operation(summary = "Запрос письма на восстановление пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Письмо отправлено", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    void getPasswordRecoveryMail(@RequestBody PasswordRecoveryDto passwordRecoveryDto);

    @PostMapping("password/recovery/{linkId}")
    @Operation(summary = "Устновка нового пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль изменён", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    void setPassword(@PathVariable String linkId, @RequestBody NewPasswordDto newPasswordDto);

}

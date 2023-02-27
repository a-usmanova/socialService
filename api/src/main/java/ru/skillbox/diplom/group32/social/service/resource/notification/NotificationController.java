package ru.skillbox.diplom.group32.social.service.resource.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group32.social.service.model.notification.*;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

@RestController
@RequestMapping(WebConstant.VERSION_URL + "/notifications")
@Tag(name = "Notification service", description = "Работа с уведомлениями")
public interface NotificationController {

    @GetMapping("/settings")
    @Operation(summary = "Получение настроек уведомлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Настройки получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NotificationSettingsDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<NotificationSettingsDto> getSettings();

    @PutMapping("/settings")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменение настроек уведомлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Настройки изменены", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    void updateSettings(@RequestBody NotificationSettingDto setting);

    @GetMapping
    @Operation(summary = "Получение списка уведомлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Уведомления получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NotificationsDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<NotificationsDto> getNotifcations();

    @GetMapping("/count")
    @Operation(summary = "Получение количества уведомлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Количество уведомлений получено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NotificationCountDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<NotificationCountDto> getNotificationsCount();

    @PostMapping("/add")
    @Operation(summary = "Добавление уведомления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомление добавлено", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    void addNotification(@RequestBody EventNotification notification);

}

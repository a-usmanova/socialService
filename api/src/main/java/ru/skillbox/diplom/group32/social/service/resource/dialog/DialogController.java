package ru.skillbox.diplom.group32.social.service.resource.dialog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.DialogsRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.MessagesRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.StatusMessageReadRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.UnreadCountRs;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

@Tag(name = "Dialog service", description = "Работа с диалогами")
@RequestMapping(value = WebConstant.VERSION_URL  + "/dialogs")
public interface DialogController {

    @GetMapping(value = "/unreaded")
    @Operation(summary = "Получение количества непрочитанных сообщений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Количество непрочитанных сообщений получено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {UnreadCountRs.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<UnreadCountRs> getUnreadMessageCount();

    @GetMapping
    @Operation(summary = "Получение списка диалогов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Список диалогов получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {DialogsRs.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<DialogsRs> getAllDialogs(@RequestParam(defaultValue = "0") Integer offset,
                                            @RequestParam(defaultValue = "20") Integer itemPerPag);

    @GetMapping(value = "/messages")
    @Operation(summary = "Получение списка сообщений диалога")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Список сообщений получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {MessagesRs.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = ""))})
    ResponseEntity<MessagesRs> getAllMessages(@RequestParam Long companionId,
                                              @RequestParam(defaultValue = "0") Integer offset,
                                              @RequestParam(defaultValue = "20") Integer itemPerPage);

    @PutMapping(value = "/{companionId}")
    @Operation(summary = "Обновление статуса сообщений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Статус сообщений обновлен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {StatusMessageReadRs.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<StatusMessageReadRs> setStatusMessageRead(@PathVariable Long companionId);
}

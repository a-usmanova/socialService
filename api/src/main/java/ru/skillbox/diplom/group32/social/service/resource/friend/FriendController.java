package ru.skillbox.diplom.group32.social.service.resource.friend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendDto;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.base.BaseController;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

import java.util.List;

@Tag(name = "Friend service", description = "Сервис друзей. Под друзьями понимается состояние отношений между пользователями: Дружба, Исходящая или Входящая заявка, Рекомендация, Блокировка")
@RequestMapping(value = WebConstant.VERSION_URL + "/friends")
public interface FriendController extends BaseController<FriendDto, FriendSearchDto> {
    @Override
    @GetMapping(value = "/{id}")
    @Operation(summary = "Получение друга по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Друг получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Друг не найден", content = @Content(mediaType = ""))})
    ResponseEntity<FriendDto> getById(@Schema(description = "id друга") @PathVariable Long id);

    @Override
    @GetMapping
    @Operation(summary = "Получение друзей по различным запросам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Друзья получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<Page<FriendDto>> getAll(FriendSearchDto searchDto, Pageable page);

    @GetMapping(value = "/recommendations")
    @Operation(summary = "Получение рекомендаций.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Рекомендации получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<List<FriendDto>> getRecommendation(FriendSearchDto searchDto);

    @GetMapping(value = "/friendId")
    @Operation(summary = "Получение id друзей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "id друзей получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<List<Long>> getFriendId();

    @GetMapping(value = "/count")
    @Operation(summary = "Получение количества входящих заявок в друзья")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Количество заявок получено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<Long> count();

    @GetMapping(value = "/blockFriendId")
    @Operation(summary = "Получение id заблокированных пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "id заблокированных пользователей получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<List<Long>> getBlockedFriendsIds();

    @PostMapping(value = "{id}/request")
    @Operation(summary = "Отправка заявки в друзья")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Заявка отправлена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Друг не найден", content = @Content(mediaType = ""))})
    ResponseEntity<List<FriendDto>> addFriend(@Schema(description = "id пользователя") @PathVariable Long id);

    @PostMapping(value = "subscribe/{id}")
    @Operation(summary = "Отправка подписки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Подписка отправлена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<List<FriendDto>> addSubscription(@Schema(description = "id пользователя") @PathVariable Long id);

    @PutMapping(value = "{id}/approve")
    @Operation(summary = "Принятие входящей заявки в друзья")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Заявка принята",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content(mediaType = ""))})
    ResponseEntity<String> approveFriend(@Schema(description = "id пользователя") @PathVariable Long id);

    @PutMapping(value = "block/{id}")
    @Operation(summary = "Блокировка либо разблокировка друга")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Блокировка или разблокировка прошла успешно",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content(mediaType = ""))})
    ResponseEntity<String> blockFriend(@Schema(description = "id пользователя") @PathVariable Long id);

    @Override
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Удален",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {FriendDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content(mediaType = ""))})
    ResponseEntity<String> deleteById(@Schema(description = "id пользователя для удаления") @PathVariable Long id);

}

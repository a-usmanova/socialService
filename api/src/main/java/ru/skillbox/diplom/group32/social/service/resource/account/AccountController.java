package ru.skillbox.diplom.group32.social.service.resource.account;

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
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.account.AccountSearchDto;
import ru.skillbox.diplom.group32.social.service.model.auth.UserDto;
import ru.skillbox.diplom.group32.social.service.resource.base.BaseController;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

@Tag(name = "Account service", description = "Работа с аккаунтом пользователя")
@RequestMapping(value = WebConstant.VERSION_URL + "/account")
public interface AccountController extends BaseController<AccountDto, AccountSearchDto> {

    @GetMapping("/me")
    @Operation(summary = "Получение аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунт получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {AccountDto.class, UserDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<AccountDto> getMe();

    @DeleteMapping("/me")
    @Operation(summary = "Удаление аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунт удален",
                    content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<String> deleteMe();

    @Override
    @PutMapping("/me")
    @Operation(summary = "Обновление аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунт обновлен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {AccountDto.class, UserDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<AccountDto> update(@RequestBody AccountDto dto);


    @GetMapping("/search")
    @Operation(summary = "Поиск аккаунта по имени и фамилии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунт найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {Pageable.class, AccountDto.class, UserDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<Page<AccountDto>> search(AccountSearchDto accountSearchDto, Pageable page);

    @GetMapping(value = "/{id}")
    @Operation(summary = "Получение аккаунта по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунт получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {AccountDto.class, UserDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Аккаунт не найден", content = @Content(mediaType = ""))})
    ResponseEntity<AccountDto> getById(@Schema(description = "id Аккаунта") @PathVariable Long id);
}

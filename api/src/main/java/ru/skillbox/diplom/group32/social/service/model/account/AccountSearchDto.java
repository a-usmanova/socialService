package ru.skillbox.diplom.group32.social.service.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.util.List;

@Data
@Schema(description = "Дто для поиска по аккаунту")
public class AccountSearchDto extends BaseSearchDto {
    @Schema(description = "Для поиска по id", example = "ids: [31231, 43252, 421678]")
    private List<Long> ids;
    @Schema(description = "Исключение id`s которые заблокированны", example = "ids: [31231, 43252, 421678]")
    private List<Long> blockedByIds;
    @Schema(description = "Поиск по автору(имя или фамилия), при заполнении только поля поиск", example = "Олег")
    private String author;
    @Schema(description = "Поиск по имени", example = "Олег")
    private String firstName;
    @Schema(description = "Поиск по фамилии", example = "Тактаров")
    private String lastName;
    @Schema(description = "Поиск по городу", example = "Москва")
    private String city;
    @Schema(description = "Поиск по стране", example = "Россия")
    private String country;
    @Schema(description = "Для исключения заблокированных пользователей", example = "false")
    private Boolean isBlocked;
    @Schema(description = "Поиск по возрасту конец диапазона", example = "30")
    private Integer ageTo;
    @Schema(description = "Поиск по возрасту начало диапазона", example = "20")
    private Integer ageFrom;

}

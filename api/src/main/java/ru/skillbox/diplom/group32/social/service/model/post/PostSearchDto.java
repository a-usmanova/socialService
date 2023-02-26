package ru.skillbox.diplom.group32.social.service.model.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто для поиска постов")
public class PostSearchDto extends BaseSearchDto {

    @Schema(description = "Айдишники постов")
    private List<Long> ids;
    @Schema(description = "Айдишники аккаунтов авторов")
    private List<Long> accountIds;
    @Schema(description = "Айдишники заблокированных авторов")
    private List<Long> blockedIds;
    @Schema(description = "Автор")
    private String author;
    @Schema(description = "с друзьями?")
    private Boolean withFriends;
    @Schema(description = "теги")
    private Set<String> tags = new HashSet<>();
    @Schema(description = "от даты")
    private ZonedDateTime dateFrom;
    @Schema(description = "до даты")
    private ZonedDateTime dateTo;
}

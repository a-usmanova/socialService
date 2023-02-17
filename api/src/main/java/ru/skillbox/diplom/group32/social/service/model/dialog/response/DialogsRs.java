package ru.skillbox.diplom.group32.social.service.model.dialog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Ответ на запрос получения списка диалогов пользователя")
public class DialogsRs extends BaseResponse {

    public DialogsRs(String error, String errorDescription, Long timestamp) {
        super(error, errorDescription, timestamp);
    }
    @Schema(description = "Количество диалогов пользователя в списке", example = "10")
    private Long total;

    @Schema(description = "Отступ от начала списка", example = "0")
    private Integer offset;

    @Schema(description = "Количество диалогов пользователя на страницу", example = "20")
    private Integer perPage;

    @Schema(description = "Id текущего пользователя", example = "55")
    private Long currentUserId;

    @Schema(description = "Список диалогов пользователя")
    private List<DialogDto> data;


}

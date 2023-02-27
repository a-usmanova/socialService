package ru.skillbox.diplom.group32.social.service.model.dialog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.base.BaseResponse;
import ru.skillbox.diplom.group32.social.service.model.dialog.unreadCountDto.UnreadCountDto;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Ответ на запрос получения количества непрочитанных сообщений диалога")
public class UnreadCountRs extends BaseResponse {

    public UnreadCountRs(String error, String errorDescription, Long timestamp) {
        super(error, errorDescription, timestamp);
    }
    @Schema(description = "Дто количества непрочитанных сообщений")
    private UnreadCountDto data;

}

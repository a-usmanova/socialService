package ru.skillbox.diplom.group32.social.service.model.dialog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто диалога")
public class DialogDto extends BaseDto {

    @Schema(description = "Количество непрочитанных сообщений диалога", example = "10")
    private Long unreadCount;
    @Schema(description = "Собеседник, id аккаунта")
    private AccountDto conversationPartner;
    @Schema(description = "Последнее сообщение диалога")
    private MessageDto lastMessage;

}

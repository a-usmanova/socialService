package ru.skillbox.diplom.group32.social.service.model.dialog;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;

import java.util.List;

@Data
@RequiredArgsConstructor
public class DialogSearchDto extends BaseSearchDto {

    private Long conversationPartnerId;
    private Long userId;

}

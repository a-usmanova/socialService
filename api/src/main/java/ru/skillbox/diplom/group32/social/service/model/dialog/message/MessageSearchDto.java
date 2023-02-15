package ru.skillbox.diplom.group32.social.service.model.dialog.message;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

@Data
@RequiredArgsConstructor
public class MessageSearchDto extends BaseSearchDto {

    private Long dialogId;
    private Long authorId;
    private Long recipientId;
    private ReadStatusDto readStatus;

}

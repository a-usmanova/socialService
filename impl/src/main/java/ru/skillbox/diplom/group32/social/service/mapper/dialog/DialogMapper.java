package ru.skillbox.diplom.group32.social.service.mapper.dialog;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogMessage;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogMessageDto;

@Mapper(componentModel = "spring")
public interface DialogMapper {

    @Mapping(target = "data", source = "dialogMessageDto")
    @Mapping(target = "accountId", source = "recipientId")
    DialogMessage dialogMessageDtoToMessage(DialogMessageDto dialogMessageDto);

}

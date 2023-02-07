package ru.skillbox.diplom.group32.social.service.mapper.dialog.dialogMessage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.dialog.dialogMessage.DialogMessage;
import ru.skillbox.diplom.group32.social.service.model.dialog.dialogMessage.DialogMessageDto;

@Mapper(componentModel = "spring")
public interface DialogMessageMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    DialogMessageDto convertToDto(DialogMessage dialogMessage);

    @Mapping(target = "isDeleted", constant = "false")
    DialogMessage convertToEntity(DialogMessageDto dialogMessageDto);

}

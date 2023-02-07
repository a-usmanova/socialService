package ru.skillbox.diplom.group32.social.service.mapper.dialog;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.dialog.Dialog;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogDto;

@Mapper(componentModel = "spring")
public interface DialogMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    DialogDto convertToDto(Dialog dialog);

    @Mapping(target = "isDeleted", constant = "false")
    Dialog convertToEntity(DialogDto dialogDto);

}

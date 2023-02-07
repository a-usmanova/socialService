package ru.skillbox.diplom.group32.social.service.mapper.dialog.message;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.Message;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.ReadStatus;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.ReadStatusDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.messageShortDto.MessageShortDto;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    MessageDto convertToDto(Message message);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "readStatus", constant = "SENT")
    Message convertToEntity(MessageDto messageDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    MessageShortDto convertToMessageShortDto(Message message);

    ReadStatusDto convertReadStatusToDto(ReadStatus readStatus);

    ReadStatus convertReadStatusToEntity(ReadStatusDto readStatusDto);

}

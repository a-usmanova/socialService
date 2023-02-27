package ru.skillbox.diplom.group32.social.service.mapper.dialog.message;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.Message;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.ReadStatus;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.ReadStatusDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.messageShortDto.MessageShortDto;

import java.time.ZoneOffset;

@Mapper(componentModel = "spring", imports = ZoneOffset.class)
public interface MessageMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    MessageDto convertToDto(Message message);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "readStatus", constant = "SENT")
    Message convertToEntity(MessageDto messageDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "time", expression = "java(message.getTime().toInstant().getEpochSecond())")
    MessageShortDto convertToMessageShortDto(Message message);

    ReadStatus convertReadStatusToEntity(ReadStatusDto readStatusDto);

}

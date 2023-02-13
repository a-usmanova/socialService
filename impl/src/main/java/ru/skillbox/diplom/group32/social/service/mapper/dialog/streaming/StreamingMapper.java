package ru.skillbox.diplom.group32.social.service.mapper.dialog.streaming;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;
import ru.skillbox.diplom.group32.social.service.model.streaming.StreamingDataDto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring",imports = {ZonedDateTime.class, Instant.class, ZoneId.class})
public interface StreamingMapper {

    @Mapping(target = "time", expression = "java(ZonedDateTime.ofInstant(Instant.ofEpochMilli(streamingDataDto.getTime()), ZoneId.systemDefault()))")
    MessageDto convertToMessageDto(StreamingDataDto streamingDataDto);


}

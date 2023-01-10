package ru.skillbox.diplom.group32.social.service.mapper.like;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.like.Like;
import ru.skillbox.diplom.group32.social.service.model.like.LikeDto;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    LikeDto convertToDto(Like like);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    Like convertToEntity(LikeDto likeDto);
}

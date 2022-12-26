package ru.skillbox.diplom.group32.social.service.mapper.tag;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag;
import ru.skillbox.diplom.group32.social.service.model.tag.TagDto;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TagMapper {
    @Mapping(target = "id", source = "id")
    TagDto convertToDto(Tag tag);

    @InheritInverseConfiguration
    Tag convertToEntity(TagDto tagDto);

    List<TagDto> convertToDtoList(List<Tag> tagSet);
    List<Tag> convertToEntityList(List<TagDto> tagDtoSet);
}

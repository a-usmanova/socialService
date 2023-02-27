package ru.skillbox.diplom.group32.social.service.mapper.tag;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag;
import ru.skillbox.diplom.group32.social.service.model.tag.TagDto;

@Mapper(componentModel = "spring")
public interface TagMapper {
    @Mapping(target = "id", source = "id")
    TagDto convertToDto(Tag tag);

    @InheritInverseConfiguration
    Tag convertToEntity(TagDto tagDto);

}

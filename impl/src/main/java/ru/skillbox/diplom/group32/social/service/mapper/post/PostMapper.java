package ru.skillbox.diplom.group32.social.service.mapper.post;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.post.Post;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {


    @Mapping(target = "id", source = "id")
    PostDto  convertToDto(Post post);

    @InheritInverseConfiguration
    Post convertToEntity(PostDto postDto);

    List<PostDto> convertToDtoList(List<Post> postList);
}

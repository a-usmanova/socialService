package ru.skillbox.diplom.group32.social.service.mapper.comment;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.post.comment.Comment;
import ru.skillbox.diplom.group32.social.service.model.post.comment.CommentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", source = "id")
    CommentDto convertToDto(Comment comment);

    @InheritInverseConfiguration
    Comment convertToEntity(CommentDto commentDto);

    List<CommentDto> convertToDtoList(List<Comment> commentList);

}


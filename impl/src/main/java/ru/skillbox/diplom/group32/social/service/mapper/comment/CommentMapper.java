package ru.skillbox.diplom.group32.social.service.mapper.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.post.comment.Comment;
import ru.skillbox.diplom.group32.social.service.model.post.comment.CommentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", source = "id")
    CommentDto convertToDto(Comment comment);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "imagePath", constant = "")
    @Mapping(target = "time", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "likeAmount", constant = "0L")
    @Mapping(target = "commentsCount", constant = "0L")
    @Mapping(target = "myLike", constant = "false")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "authorId", expression = "java(ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext())")
    Comment convertToEntity(CommentDto commentDto);

    List<CommentDto> convertToDtoList(List<Comment> commentList);

}


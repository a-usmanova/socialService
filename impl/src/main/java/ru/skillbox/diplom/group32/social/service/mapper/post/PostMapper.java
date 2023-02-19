package ru.skillbox.diplom.group32.social.service.mapper.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.account.AccountSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.Post;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;

import java.util.List;



@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    PostDto convertToDto(Post post);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "timeChanged", expression = "java(ZonedDateTime.now())")
    Post convertToEntity(PostDto postDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "authorId", expression = "java(ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext())")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "publishDate", expression = "java((postDto.getPublishDate()==null) ? ZonedDateTime.now() : postDto.getPublishDate())")
    @Mapping(target = "myLike", constant = "false")
    @Mapping(target = "commentsCount", constant = "0L")
    @Mapping(target = "likeAmount", constant = "0L")
    @Mapping(target = "time", expression = "java((postDto.getPublishDate()==null) ? ZonedDateTime.now() : postDto.getPublishDate())")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "type", expression = "java(Type.POSTED)")
    @Mapping(target = "tags", ignore = true)
    Post convertToEntityCreated(PostDto postDto);


    @Mapping(target = "tags", ignore = true)
    List<PostDto> convertToDtoList(List<Post> postList);

    AccountSearchDto convertPostSearchDtoToAccountSearchDto(PostSearchDto searchDto);

}

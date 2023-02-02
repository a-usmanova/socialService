package ru.skillbox.diplom.group32.social.service.mapper.friend;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.auth.UserDto;
import ru.skillbox.diplom.group32.social.service.model.friend.Friend;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(target = "id", source = "id")
    FriendDto convertToDto(Friend friend);

    @Mapping(target = "toAccountId", source = "id")
    @Mapping(target = "statusCode", expression = "java(StatusCode.REQUEST_TO)")
    @Mapping(target = "fromAccountId", expression = "java(ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext())")
    Friend userDtoToFriend(UserDto userDto);

    @Mapping(target = "fromAccountId", source = "id")
    @Mapping(target = "statusCode", expression = "java(StatusCode.REQUEST_FROM)")
    @Mapping(target = "toAccountId", expression = "java(ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext())")
    Friend userDtoToFriendFrom(UserDto userDto);

    @Mapping(target = "fromAccountId", source = "id")
    FriendDto userDtoToFriendDto(UserDto userDto);

    @InheritInverseConfiguration
    Friend convertToEntity(FriendDto friendDto);

    List<FriendDto> convertToDtoList(List<Friend> friendList);
    List<Friend> convertToEntityList(List<FriendDto> friendDtoList);

}

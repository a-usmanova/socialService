package ru.skillbox.diplom.group32.social.service.mapper.friend;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.account.AccountSearchDto;
import ru.skillbox.diplom.group32.social.service.model.auth.UserDto;
import ru.skillbox.diplom.group32.social.service.model.friend.Friend;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendDto;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendSearchDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(target = "id", source = "id")
    FriendDto convertToDto(Friend friend);

    @Mapping(target = "fromAccountId", source = "id")
    FriendDto userDtoToFriendDto(UserDto userDto);

    @InheritInverseConfiguration
    Friend convertToEntity(FriendDto friendDto);

    List<FriendDto> convertToDtoList(List<Friend> friendList);

    AccountSearchDto friendSearchDtoToAccountSearchDto(FriendSearchDto searchDto);

    FriendDto accountDtoToFriendDto(AccountDto accountDto);

}

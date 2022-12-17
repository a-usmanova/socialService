package ru.skillbox.diplom.group32.social.service.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.auth.UserDto;
import ru.skillbox.diplom.group32.social.service.model.auth.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);

}

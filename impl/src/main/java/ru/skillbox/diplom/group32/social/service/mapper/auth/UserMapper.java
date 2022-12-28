package ru.skillbox.diplom.group32.social.service.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.auth.RegistrationDto;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.model.auth.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "password", source = "password1")
    UserDto registrationDtoToUserDto(RegistrationDto registrationDto);

}

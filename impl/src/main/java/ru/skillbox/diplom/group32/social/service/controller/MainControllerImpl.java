package ru.skillbox.diplom.group32.social.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.UserDto;
import ru.skillbox.diplom.group32.social.service.resource.MainController;
import ru.skillbox.diplom.group32.social.service.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainControllerImpl implements MainController {

    final UserService userService;

    @Override
    public UserDto createUser(String name, Integer age) {
        UserDto userDto = new UserDto(name, age);
        log.info("Send User - " + userDto);
        UserDto result = userService.createUser(userDto);
        log.info("Created User - " + result);
        return result;
    }

    @Override
    public UserDto getUser(Long id) {
        UserDto result = userService.getUser(id);
        log.info("User from DB - " + result);
        return result;
    }

    @Override
    public void deleteUser(UserDto userDto) {
        log.info("Send User to delete - " + userDto);
        userService.deleteUser(userDto);
    }

    @Override
    public void deleteUserById(Long id) {
        userService.deleteUserById(id);
    }

    @Override
    public void hardDeleteUser(UserDto userDto) {
        userService.hardDeleteUser(userDto);
    }

    @Override
    public void hardDeleteUserById(Long id) {
        userService.hardDeleteUserById(id);
    }
}

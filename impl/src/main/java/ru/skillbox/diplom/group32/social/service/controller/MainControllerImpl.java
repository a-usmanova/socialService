/*
package ru.skillbox.diplom.group32.social.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.SearchDto;
import ru.skillbox.diplom.group32.social.service.model.security.UserDto;
import ru.skillbox.diplom.group32.social.service.resource.MainController;
import ru.skillbox.diplom.group32.social.service.service.auth.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainControllerImpl implements MainController {

    final UserService userService;

    @Override
    public ResponseEntity<UserDto> create(UserDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> getById(Long id) {
        UserDto result = userService.getUser(id);
        log.info("User from DB - " + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<UserDto>> getAll(SearchDto searchDto) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> update(UserDto dto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    //    @Override
//    public Respo UserDto createUser(String name, Integer age) {
//        UserDto userDto = new UserDto(name, age);
//        log.info("Send User - " + userDto);
//        UserDto result = userService.createUser(userDto);
//        log.info("Created User - " + result);
//        return result;
//    }
//
//    @Override
//    public UserDto getUser(Long id) {
//        UserDto result = userService.getUser(id);
//        log.info("User from DB - " + result);
//        return result;
//    }
//
//    @Override
//    public void deleteUser(UserDto userDto) {
//        log.info("Send User to delete - " + userDto);
//        userService.deleteUser(userDto);
//    }
//
//    @Override
//    public void deleteUserById(Long id) {
//        userService.deleteUserById(id);
//    }

}
*/

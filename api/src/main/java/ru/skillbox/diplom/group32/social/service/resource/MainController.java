package ru.skillbox.diplom.group32.social.service.resource;

import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.UserDto;

@RequestMapping("/user")
public interface MainController {

    @PostMapping(value = "/create")
    UserDto createUser(@RequestParam(value = "name") String title, @RequestParam(value = "age") Integer age);


    @GetMapping(value = "/get/{id}")
    UserDto getUser(@PathVariable Long id);


    @DeleteMapping(value = "/delete")
    void deleteUser(UserDto userDto);

    @DeleteMapping(value = "/delete/{id}")
    void deleteUserById(@PathVariable Long id);

    @DeleteMapping(value = "/hard-delete")
    void hardDeleteUser(UserDto userDto);

    @DeleteMapping(value = "/hard-delete/{id}")
    void hardDeleteUserById(@PathVariable Long id);

}

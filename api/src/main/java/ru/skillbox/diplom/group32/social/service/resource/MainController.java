package ru.skillbox.diplom.group32.social.service.resource;

import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.UserDto;

@RequestMapping("/user")
public interface MainController {

    @PostMapping(value = "/create")
    UserDto createUser(@RequestParam(value = "title") String title, @RequestParam(value = "age") Integer age);


    @GetMapping(value = "/get/{value}")
    UserDto getUser(@PathVariable Integer value);


}

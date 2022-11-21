package ru.skillbox.diplom.group32.social.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private Integer id;

    private String name;

    private Integer age;

    public UserDto(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}

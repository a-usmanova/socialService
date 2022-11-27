package ru.skillbox.diplom.group32.social.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Data
@NoArgsConstructor
public class UserDto extends BaseDto {

    private String name;
    private Integer age;

    public UserDto(Long id, String name, Integer age) {
        super.setId(id);
        this.name = name;
        this.age = age;
    }
    public UserDto(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}

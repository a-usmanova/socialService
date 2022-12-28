package ru.skillbox.diplom.group32.social.service.model;

import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.util.List;

@Data

public class UserSearchDto extends BaseSearchDto {

    private List<String> names;
    private String firstName;
    //This field has deleted in User & userDto
    private Integer age;
}


package ru.skillbox.diplom.group32.social.service.model;

import lombok.*;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto extends BaseSearchDto {

    private List<String> names;
    private String name;
    private Integer age;
}


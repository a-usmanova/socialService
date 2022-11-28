package ru.skillbox.diplom.group32.social.service.model;

import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto {

    private String name;
    private Integer age;
}

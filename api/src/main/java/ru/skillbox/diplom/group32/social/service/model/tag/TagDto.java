package ru.skillbox.diplom.group32.social.service.model.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Schema(description = "Дто для тега")
@Data
public class TagDto extends BaseDto {

    @Schema(description = "Имя тега")
    private String name;

}

package ru.skillbox.diplom.group32.social.service.model.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Дто страны с городами с сайта hh.ru")
public class AreaJsonDto {

    Long id;
    @JsonProperty("parent_id")
    Long parentId;

    String name;

    AreaJsonDto[] areas;
}

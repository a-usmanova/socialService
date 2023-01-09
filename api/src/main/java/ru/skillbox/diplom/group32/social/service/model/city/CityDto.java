package ru.skillbox.diplom.group32.social.service.model.city;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Data
@Schema(description = "Дто города")
public class CityDto extends BaseDto {
  @Schema(description = "Наименование города")
  private String title;
  @Schema(description = "Id страны")
  private Long countryId;
}

package ru.skillbox.diplom.group32.social.service.model.city;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;

@Data
@Schema(description = "Дто города")
public class CityDto extends BaseDto {
  @Schema(description = "Наименование города", example = "Москва")
  private String title;
  @Schema(description = "Id страны", example = "42352")
  private Long countryId;
}

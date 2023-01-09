package ru.skillbox.diplom.group32.social.service.model.country;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;
import ru.skillbox.diplom.group32.social.service.model.city.CityDto;

@Data
@Schema(description = "Дто аккаунта")
public class CountryDto extends BaseDto {
  @Schema(description = "Наименование страны")
  private String title;
  @Schema(description = "Города страны")
  private List<CityDto> cities;
}
